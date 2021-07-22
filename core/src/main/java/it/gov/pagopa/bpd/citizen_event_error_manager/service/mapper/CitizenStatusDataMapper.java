package it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import org.springframework.stereotype.Service;

@Service
public class CitizenStatusDataMapper implements ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> {

    @Override
    public CitizenStatusData mapTo(CitizenStatusErrorRecord entity) {
        return CitizenStatusData.builder()
                .fiscalCode(entity.getFiscalCode())
                .updateDateTime(entity.getUpdateDateTime())
                .applyTo(entity.getOrigin())
                .enabled(entity.getStatus())
                .build();
    }

    @Override
    public CitizenStatusErrorRecord mapFrom(CitizenStatusData entity) {
        return CitizenStatusErrorRecord.builder()
                .fiscalCode(entity.getFiscalCode())
                .updateDateTime(entity.getUpdateDateTime())
                .status(entity.isEnabled())
                .origin(entity.getApplyTo())
                .build();
    }

}
