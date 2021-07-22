package it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model;

import lombok.*;

import java.time.OffsetDateTime;

/**
 * Model for transaction to be sent in the outbound channel
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode", "enabled", "updateDateTime", "applyTo"}, callSuper = false)
public class CitizenStatusData {

    private String fiscalCode;
    private boolean enabled;
    private OffsetDateTime updateDateTime;
    private String applyTo = "all";

}
