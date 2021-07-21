package it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
