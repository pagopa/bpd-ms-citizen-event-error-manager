package it.gov.pagopa.bpd.transaction_error_manager.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CitizenEventError {

    String fiscalCode;

    Boolean enabled;

    String applyTo;

    OffsetDateTime updateDateTime;

    String origin;

    String exceptionMessage;

}
