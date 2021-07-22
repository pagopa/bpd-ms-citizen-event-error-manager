package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.transformer.HeaderAwareRequestTransformer;
import lombok.SneakyThrows;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the BpdCitizenStatusDataPublisherService, defines the service used for the interaction
 * with the CitizenStatusPublisherConnector
 */

@Service
class BpdCitizenStatusDataPublisherServiceImpl implements BpdCitizenStatusDataPublisherService {

    private final CitizenStatusPublisherConnector citizenStatusPublisherConnector;
    private final HeaderAwareRequestTransformer<CitizenStatusData> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Autowired
    public BpdCitizenStatusDataPublisherServiceImpl(
            CitizenStatusPublisherConnector citizenStatusPublisherConnector,
            HeaderAwareRequestTransformer<CitizenStatusData> simpleEventRequestTransformer,
            SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.citizenStatusPublisherConnector = citizenStatusPublisherConnector;
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }

    /**
     * Calls the BpdCitizenStatusDataPublisherService, passing the transaction to be used as message payload
     *
     * @param citizenStatusData OutgoingTransaction instance to be used as payload for the outbound channel used bu the related connector
     */

    @SneakyThrows
    @Override
    public void publishBpdCitizenEvent(CitizenStatusData citizenStatusData, RecordHeaders recordHeaders) {
        citizenStatusPublisherConnector.doCall(
                citizenStatusData, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }
}
