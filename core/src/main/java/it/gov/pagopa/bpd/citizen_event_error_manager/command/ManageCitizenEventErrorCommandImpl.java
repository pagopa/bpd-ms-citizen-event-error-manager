package it.gov.pagopa.bpd.citizen_event_error_manager.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventErrorCommandModel;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.BpdCitizenStatusDataPublisherService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper.ModelMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Base implementation of the ManageCitizenEventErrorCommand, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class ManageCitizenEventErrorCommandImpl extends BaseCommand<Boolean> implements ManageCitizenEventErrorCommand {

    private CitizenStatusErrorRecordService citizenStatusErrorRecordService;
    private BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService;
    private CitizenEventErrorCommandModel citizenEventErrorCommandModel;
    private ModelMapper<CitizenEventError, CitizenStatusErrorRecord> citizenStatusErrorRecordModelMapper;
    private ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper;
    private Integer maxRetries;
    private String originsToAutoResend;


    public ManageCitizenEventErrorCommandImpl(CitizenEventErrorCommandModel citizenEventErrorCommandModel) {
        this.citizenEventErrorCommandModel = citizenEventErrorCommandModel;
    }

    public ManageCitizenEventErrorCommandImpl(
            CitizenEventErrorCommandModel citizenEventErrorCommandModel,
            CitizenStatusErrorRecordService citizenStatusErrorRecordService,
            BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService,
            ModelMapper<CitizenEventError, CitizenStatusErrorRecord> citizenStatusErrorRecordModelMapper,
            ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper,
            Integer maxRetries,
            String originsToAutoResend) {
        this.citizenEventErrorCommandModel = citizenEventErrorCommandModel;
        this.citizenStatusErrorRecordService = citizenStatusErrorRecordService;
        this.bpdCitizenStatusDataPublisherService = bpdCitizenStatusDataPublisherService;
        this.citizenStatusErrorRecordModelMapper = citizenStatusErrorRecordModelMapper;
        this.citizenStatusDataModelMapper = citizenStatusDataModelMapper;
        this.maxRetries = maxRetries;
        this.originsToAutoResend = originsToAutoResend;
    }

    /**
     * Implementation of the MEDA Command doExecute method, contains the logic for the inbound transaction
     * management, calls the REST endpoint to check if it the related paymentInstrument is active, and eventually
     * sends the Transaction to the proper outbound channel. In case of an error, send a
     *
     * @return boolean to indicate if the command is successfully executed
     */

    @SneakyThrows
    @Override
    public Boolean doExecute() {

        CitizenEventError citizenEventError = citizenEventErrorCommandModel.getPayload();

        try {

            Optional<CitizenStatusErrorRecord> citizenStatusErrorRecordOpt =
                    citizenStatusErrorRecordService.findExistingRecord(
                    citizenEventError.getFiscalCode(),
                    citizenEventError.getOrigin(),
                    citizenEventError.getEnabled(),
                    citizenEventError.getUpdateDateTime()
            );

            CitizenStatusErrorRecord citizenStatusErrorRecord = citizenStatusErrorRecordOpt.orElseGet(
                    () -> citizenStatusErrorRecordModelMapper.mapTo(citizenEventError));


            List<String> originsToAutoResendList = originsToAutoResend != null ?
                    new ArrayList<>(Arrays.asList(originsToAutoResend.split(","))) : Collections.emptyList();


            if (citizenStatusErrorRecord.getNumberOfRetries() <= maxRetries
                    && originsToAutoResendList.contains(citizenEventError.getOrigin())) {
                CitizenStatusData citizenStatusData = citizenStatusDataModelMapper.mapTo(citizenStatusErrorRecord);
                bpdCitizenStatusDataPublisherService.publishBpdCitizenEvent(
                        citizenStatusData, new RecordHeaders());
                citizenStatusErrorRecord.setNumberOfRetries(citizenStatusErrorRecord.getNumberOfRetries()+1);
                citizenStatusErrorRecord.setLastResubmitDate(OffsetDateTime.now());
            }

            citizenStatusErrorRecord.setAvailableForResubmit(false);
            citizenStatusErrorRecord.setExceptionMessage(citizenEventError.getExceptionMessage());
            citizenStatusErrorRecordService.saveCitizenStatusErrorRecordService(citizenStatusErrorRecord);

            return true;

        } catch (Exception e) {

            if (citizenEventError != null) {
                logger.error(e.getMessage(), e);
            }

            throw e;
        }

    }

    @Value("${it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService.numRetries}")
    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Value("${it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService.originsToAutoResend}")
    public void setOriginsToAutoResend(String originsToAutoResend) {
        this.originsToAutoResend = originsToAutoResend;
    }

    @Autowired
    public void setCitizenStatusErrorRecordService(
            CitizenStatusErrorRecordService citizenStatusErrorRecordService) {
        this.citizenStatusErrorRecordService = citizenStatusErrorRecordService;
    }

    @Autowired
    public void setBpdCitizenStatusDataPublisherService(
            BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService) {
        this.bpdCitizenStatusDataPublisherService = bpdCitizenStatusDataPublisherService;
    }

    @Autowired
    public void setCitizenStatusErrorRecordModelMapper(
            ModelMapper<CitizenEventError, CitizenStatusErrorRecord> citizenStatusErrorRecordModelMapper) {
        this.citizenStatusErrorRecordModelMapper = citizenStatusErrorRecordModelMapper;
    }

    @Autowired
    public void setCitizenStatusDataModelMapper(
            ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper) {
        this.citizenStatusDataModelMapper = citizenStatusDataModelMapper;
    }

}
