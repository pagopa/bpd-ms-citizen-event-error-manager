package it.gov.pagopa.bpd.citizen_event_error_manager.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.eventlistener.BaseConsumerAwareEventListener;
import it.gov.pagopa.bpd.citizen_event_error_manager.listener.factory.ModelFactory;
import it.gov.pagopa.bpd.transaction_error_manager.command.SaveTransactionRecordCommand;
import it.gov.pagopa.bpd.transaction_error_manager.model.CitizenEventErrorCommandModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Class Extending the MEDA BaseEventListener, manages the inbound requests, and calls on the appropriate
 * command for the check and send logic associated to the Transaction payload
 */

@Service
@Slf4j
public class OnCitizenStatusErrorRequestListener extends BaseConsumerAwareEventListener {

    private final ModelFactory<Pair<byte[], Headers>, CitizenEventErrorCommandModel> citizenEventErrorCommandModelModelFactory;
    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public OnCitizenStatusErrorRequestListener(
            ModelFactory<Pair<byte[], Headers>, CitizenEventErrorCommandModel> citizenEventErrorCommandModelModelFactory,
            BeanFactory beanFactory,
            ObjectMapper objectMapper) {
        this.citizenEventErrorCommandModelModelFactory = citizenEventErrorCommandModelModelFactory;
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
    }

    /**
     * Method called on receiving a message in the inbound queue,
     * that should contain a JSON payload containing transaction data,
     * calls on a command to execute the check and send logic for the input Transaction data
     * In case of error, sends data to an error channel
     *
     * @param payload Message JSON payload in byte[] format
     * @param headers Kafka headers from the inbound message
     */

    @SneakyThrows
    @Override
    public void onReceived(byte[] payload, Headers headers) {

        CitizenEventErrorCommandModel citizenEventErrorCommandModel = null;

        try {

            if (log.isDebugEnabled()) {
                log.debug("Processing new request on inbound queue");
            }

            citizenEventErrorCommandModel = citizenEventErrorCommandModelModelFactory
                    .createModel(Pair.of(payload, headers));
            SaveTransactionRecordCommand command = beanFactory.getBean(
                    SaveTransactionRecordCommand.class, citizenEventErrorCommandModel);

            if (!command.execute()) {
                throw new Exception("Failed to execute OnCitizenStatusErrorRequestListener");
            }

            if (log.isDebugEnabled()) {
                log.debug("OnCitizenStatusErrorRequestListener successfully executed for inbound message");
            }

        } catch (Exception e) {

            String payloadString = "null";
            String error = "Unexpected error during transaction processing";

            try {
                payloadString = new String(payload, StandardCharsets.UTF_8);
            } catch (Exception e2) {
                if (logger.isErrorEnabled()) {
                    logger.error("Something gone wrong converting the payload into String", e2);
                }
            }

            if (citizenEventErrorCommandModel != null && citizenEventErrorCommandModel.getPayload() != null) {
                payloadString = new String(payload, StandardCharsets.UTF_8);
                error = String.format("Unexpected error during transaction processing: %s, %s",
                        payloadString, e.getMessage());
                throw e;
            } else if (payload != null) {
                error = String.format("Something gone wrong during the evaluation of the payload: %s, %s",
                        payloadString, e.getMessage());
                if (logger.isErrorEnabled()) {
                    logger.error(error, e);
                }
            }


        }
    }

}
