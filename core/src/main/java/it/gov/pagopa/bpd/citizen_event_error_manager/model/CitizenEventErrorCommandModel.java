package it.gov.pagopa.bpd.citizen_event_error_manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;

/**
 * Model containing the inbound message data
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenEventErrorCommandModel {

    private CitizenEventError payload;
    private Headers headers;

}
