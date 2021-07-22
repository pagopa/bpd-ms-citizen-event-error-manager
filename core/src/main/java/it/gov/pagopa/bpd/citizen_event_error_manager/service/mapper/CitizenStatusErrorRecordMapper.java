package it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import org.springframework.stereotype.Service;

@Service
public class CitizenStatusErrorRecordMapper implements ModelMapper<CitizenEventError, CitizenStatusErrorRecord> {

    @Override
    public CitizenStatusErrorRecord mapTo(CitizenEventError entity) {
        return CitizenStatusErrorRecord.builder()
                .fiscalCode(entity.getFiscalCode())
                .updateDateTime(entity.getUpdateDateTime())
                .origin(entity.getOrigin())
                .status(entity.getEnabled())
                .exceptionMessage(entity.getExceptionMessage())
                .numberOfRetries(0)
                .availableForResubmit(false)
                .build();
    }

    @Override
    public CitizenEventError mapFrom(CitizenStatusErrorRecord entity) {
        return CitizenEventError.builder()
                .fiscalCode(entity.getFiscalCode())
                .updateDateTime(entity.getUpdateDateTime())
                .enabled(entity.getStatus())
                .applyTo(entity.getOrigin())
                .exceptionMessage(entity.getExceptionMessage())
                .build();
    }

}
