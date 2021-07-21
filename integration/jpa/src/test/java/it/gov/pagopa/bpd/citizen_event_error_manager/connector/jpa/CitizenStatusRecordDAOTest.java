package it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecordId;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class CitizenStatusRecordDAOTest extends BaseCrudJpaDAOTest<CitizenStatusErrorRecordDAO, CitizenStatusErrorRecord, CitizenStatusErrorRecordId> {

    @Data
    private static class TransactionRecordCriteria implements CriteriaQuery<CitizenStatusErrorRecord> {
        private String fiscalCode;
        private Boolean status;
        private OffsetDateTime updateDateTime;
        private String origin;
    }

    @Autowired
    private CitizenStatusErrorRecordDAO dao;

    @Override
    protected CriteriaQuery<? super CitizenStatusErrorRecord> getMatchAlreadySavedCriteria() {
        TransactionRecordCriteria criteriaQuery = new TransactionRecordCriteria();
        criteriaQuery.setFiscalCode(getStoredId().getFiscalCode());
        criteriaQuery.setOrigin(getStoredId().getOrigin());
        criteriaQuery.setStatus(getStoredId().getStatus());
        criteriaQuery.setUpdateDateTime(getStoredId().getUpdateDateTime());
        return criteriaQuery;
    }

    @Override
    protected CitizenStatusErrorRecordDAO getDao() {
        return dao;
    }

    @Override
    protected void setId(CitizenStatusErrorRecord entity, CitizenStatusErrorRecordId id) {
        entity.setFiscalCode(id.getFiscalCode());
        entity.setUpdateDateTime(id.getUpdateDateTime());
        entity.setOrigin(id.getOrigin());
        entity.setStatus(id.getStatus());
    }

    @Override
    protected CitizenStatusErrorRecordId getId(CitizenStatusErrorRecord entity) {
        return CitizenStatusErrorRecordId
                .builder()
                .fiscalCode(entity.getFiscalCode())
                .updateDateTime(entity.getUpdateDateTime())
                .status(entity.getStatus())
                .origin(entity.getOrigin())
                .build();
    }

    @Override
    protected void alterEntityToUpdate(CitizenStatusErrorRecord entity) {
        entity.setAvailableForResubmit(true);
        entity.setLastResubmitDate(OffsetDateTime.now());
        entity.setUpdateDate(OffsetDateTime.now());
    }

    @Override
    protected Function<Integer, CitizenStatusErrorRecordId> idBuilderFn() {
        return (bias) -> CitizenStatusErrorRecordId
                .builder()
                .fiscalCode("fiscalCode"+bias)
                .origin("origin")
                .status(false)
                .updateDateTime(OffsetDateTime.parse("2021-01-01T00:00:00.000Z"))
                .build();
    }

    @Override
    protected String getIdName() {
        return "fiscalCode";
    }
}