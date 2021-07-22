package it.gov.pagopa.bpd.citizen_event_error_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitizenEventError {

    String fiscalCode;

    Boolean enabled;

    String applyTo;

    OffsetDateTime updateDateTime;

    String origin;

    String exceptionMessage;

}
