package it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecordId;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenStatusErrorRecordDAO extends CrudJpaDAO<CitizenStatusErrorRecord, CitizenStatusErrorRecordId> {

    List<CitizenStatusErrorRecord> findByAvailableForResubmit(Boolean toResubmit);

}
