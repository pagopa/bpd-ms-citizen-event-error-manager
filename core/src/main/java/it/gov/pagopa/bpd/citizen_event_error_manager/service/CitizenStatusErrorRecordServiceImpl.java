package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.CitizenStatusErrorRecordDAO;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
