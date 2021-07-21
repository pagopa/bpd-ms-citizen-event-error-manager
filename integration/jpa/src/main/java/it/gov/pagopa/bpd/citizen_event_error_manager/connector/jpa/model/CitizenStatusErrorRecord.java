package it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.*;
import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CitizenStatusErrorRecordId.class)
@EqualsAndHashCode(of = {"fiscalCode", "updateDateTime", "status", "origin"}, callSuper = false)
@Table(name = "bpd_citizen_status_error_record")
public class CitizenStatusErrorRecord extends BaseEntity {

    @Id
    @Column(name = "fiscal_code_s")
    private String fiscalCode;

    @Id
    @Column(name = "update_date_time_t")
    private OffsetDateTime updateDateTime;

    @Id
    @Column(name = "status_b")
    private Boolean status;

    @Id
    @Column(name = "origin_s")
    private String origin;

    @Column(name = "exception_message_s")
    private String exceptionMessage;

    @Column(name = "available_for_resubmit_b")
    private Boolean availableForResubmit;

    @Column(name = "number_of_retries_n")
    private Integer numberOfRetries;

    @Column(name = "last_resubmit_date_t")
    private OffsetDateTime lastResubmitDate;

}




