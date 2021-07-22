package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.CitizenStatusErrorRecordDAO;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecordId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @See CitizenStatusErrorRecordService
 */
@Service
class CitizenStatusErrorRecordServiceImpl extends BaseService implements CitizenStatusErrorRecordService {

    private final CitizenStatusErrorRecordDAO citizenStatusErrorRecordDAO;

    @Autowired
    public CitizenStatusErrorRecordServiceImpl(CitizenStatusErrorRecordDAO citizenStatusErrorRecordDAO) {
        this.citizenStatusErrorRecordDAO = citizenStatusErrorRecordDAO;
    }

    @Override
    public CitizenStatusErrorRecord saveCitizenStatusErrorRecordService(CitizenStatusErrorRecord transactionRecord) {
        return citizenStatusErrorRecordDAO.save(transactionRecord);
    }

    @Override
    public List<CitizenStatusErrorRecord> findRecordsToResubmit() {
        return citizenStatusErrorRecordDAO.findByAvailableForResubmit(true);
    }

    @Override
    public Optional<CitizenStatusErrorRecord> findExistingRecord(
            String fiscalCode, String origin, Boolean status, OffsetDateTime updateDateTime) {
        return citizenStatusErrorRecordDAO.findById(CitizenStatusErrorRecordId
                .builder()
                .fiscalCode(fiscalCode)
                .origin(origin)
                .status(status)
                .updateDateTime(updateDateTime)
                .build());
    }

}
