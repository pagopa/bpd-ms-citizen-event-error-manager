package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import org.apache.kafka.common.header.internals.RecordHeaders;

/**
 * public interface for the BpdCitizenStatusDatatPublisherService
 */

public interface BpdCitizenStatusDataPublisherService {

    /**
     * Method that has the logic for publishing a CitizenStatusData to the rtd outbound channel,
     * calling on the appropriate connector
     *
     * @param transaction CitizenStatusData instance to be published
     */
    void publishBpdCitizenEvent(CitizenStatusData transaction, RecordHeaders recordHeaders);

}
