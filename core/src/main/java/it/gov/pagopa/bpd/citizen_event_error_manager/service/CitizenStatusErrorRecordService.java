package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;

import java.util.List;

/**
 * A service to manage the Business Logic related to CitizenStatusErrorRecord
 */
public interface CitizenStatusErrorRecordService {

    CitizenStatusErrorRecord saveCitizenStatusErrorRecordService(CitizenStatusErrorRecord citizenStatusErrorRecord);

    List<CitizenStatusErrorRecord> findRecordsToResubmit();

}
