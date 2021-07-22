package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A service to manage the Business Logic related to CitizenStatusErrorRecord
 */
public interface CitizenStatusErrorRecordService {

    CitizenStatusErrorRecord saveCitizenStatusErrorRecordService(CitizenStatusErrorRecord citizenStatusErrorRecord);

    List<CitizenStatusErrorRecord> findRecordsToResubmit();

    Optional<CitizenStatusErrorRecord> findExistingRecord
            (String fiscalCode, String origin, Boolean status, OffsetDateTime updateDateTime);
}
