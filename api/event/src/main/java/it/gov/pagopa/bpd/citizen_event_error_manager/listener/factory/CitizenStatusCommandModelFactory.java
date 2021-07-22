package it.gov.pagopa.bpd.citizen_event_error_manager.listener.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventErrorCommandModel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of the ModelFactory interface, that maps a pair containing Kafka related byte[] payload and Headers
 * into a single model for usage inside the microservice core classes
 */

@Component
public class CitizenStatusCommandModelFactory implements
        ModelFactory<Pair<byte[], Headers>, CitizenEventErrorCommandModel> {

    private final ObjectMapper objectMapper;

    @Autowired
    public CitizenStatusCommandModelFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param requestData
     * @return instance of CitizenEventErrorCommandModel, containing a CitizenEventError instance,
     * mapped from the byte[] payload in the requestData, and the inbound Kafka headers
     */

    @Override
    public CitizenEventErrorCommandModel createModel(Pair<byte[], Headers> requestData) {
        CitizenEventError citizenEventError = parsePayload(requestData.getLeft());
        return CitizenEventErrorCommandModel.builder()
                .payload(citizenEventError)
                .headers(requestData.getRight())
                .build();
    }

    /**
     * Method containing the logic for the parsing of the byte[] payload into an instance of CitizenEventError,
     * using the ObjectMapper
     *
     * @param payload inbound JSON payload in byte[] format, defining a CitizenEventError
     * @return instance of CitizenEventError, mapped from the input json byte[] payload
     */
    private CitizenEventError parsePayload(byte[] payload) {
        String json = new String(payload, StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(json, CitizenEventError.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot parse the payload as a valid %s", CitizenEventError.class), e);
        }
    }

}
