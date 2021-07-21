package it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model;

import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CitizenStatusErrorRecordId implements Serializable {

    private String fiscalCode;
    private OffsetDateTime updateDateTime;
    private Boolean status;
    private String origin;

}
