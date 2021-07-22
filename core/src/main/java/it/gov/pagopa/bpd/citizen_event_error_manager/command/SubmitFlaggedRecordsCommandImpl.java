package it.gov.pagopa.bpd.citizen_event_error_manager.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.BpdCitizenStatusDataPublisherService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper.ModelMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Base implementation of the SubmitFlaggedRecordsCommand, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class SubmitFlaggedRecordsCommandImpl extends BaseCommand<Boolean> implements SubmitFlaggedRecordsCommand {

    private CitizenStatusErrorRecordService citizenStatusErrorRecordService;
    private BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService;
    private ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper;

    public SubmitFlaggedRecordsCommandImpl() {}

    public SubmitFlaggedRecordsCommandImpl(
            CitizenStatusErrorRecordService citizenStatusErrorRecordService,
            BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService,
            ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper) {
        this.citizenStatusErrorRecordService = citizenStatusErrorRecordService;
        this.bpdCitizenStatusDataPublisherService = bpdCitizenStatusDataPublisherService;
        this.citizenStatusDataModelMapper = citizenStatusDataModelMapper;
    }

    /**
     * Implementation of the MEDA Command doExecute method, contains the logic to resubmit flagged records,
     * through the dedicated channel
     *
     * @return boolean to indicate if the command is successfully executed
     */

    @SneakyThrows
    @Override
    public Boolean doExecute() {

        this.callAsyncService(() -> {
            try {

                List<CitizenStatusErrorRecord> citizenStatusErrorRecordList =
                        citizenStatusErrorRecordService.findRecordsToResubmit();

                for (CitizenStatusErrorRecord citizenStatusErrorRecord : citizenStatusErrorRecordList) {
                    this.callAsyncService(() -> {

                        try {

                            CitizenStatusData citizenStatusData = citizenStatusDataModelMapper
                                    .mapTo(citizenStatusErrorRecord);

                            bpdCitizenStatusDataPublisherService.publishBpdCitizenEvent(
                                    citizenStatusData, new RecordHeaders());

                            citizenStatusErrorRecord.setNumberOfRetries(
                                    citizenStatusErrorRecord.getNumberOfRetries()+1);
                            citizenStatusErrorRecord.setAvailableForResubmit(false);
                            citizenStatusErrorRecord.setLastResubmitDate(OffsetDateTime.now());
                            citizenStatusErrorRecordService.saveCitizenStatusErrorRecordService(
                                    citizenStatusErrorRecord);

                            return true;

                        } catch (Exception e) {
                            logger.error("Error occurred while attempting to submit flagged record");
                            logger.error(e.getMessage(), e);
                            throw e;
                        }

                    });

               }

                return true;

            } catch (Exception e) {
                logger.error("Error occurred while attempting to submit flagged records");
                logger.error(e.getMessage(), e);
                throw e;
            }

        });

        return true;
    }

    @Autowired
    public void setCitizenStatusDataModelMapper(
            ModelMapper<CitizenStatusErrorRecord, CitizenStatusData> citizenStatusDataModelMapper) {
        this.citizenStatusDataModelMapper = citizenStatusDataModelMapper;
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

    protected void setAsyncUtils(AsyncUtils asyncUtils) {
        this.asyncUtils = asyncUtils;
    }

}
