package it.gov.pagopa.bpd.citizen_event_error_manager.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventErrorCommandModel;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Base implementation of the ManageCitizenEventErrorCommand, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class ManageCitizenEventErrorCommandImpl extends BaseCommand<Boolean> implements ManageCitizenEventErrorCommand {

    private CitizenStatusErrorRecordService citizenStatusErrorRecordService;
    private CitizenEventErrorCommandModel citizenEventErrorCommandModel;


    public ManageCitizenEventErrorCommandImpl(CitizenEventErrorCommandModel citizenEventErrorCommandModel) {
        this.citizenEventErrorCommandModel = citizenEventErrorCommandModel;
    }

    public ManageCitizenEventErrorCommandImpl(
            CitizenEventErrorCommandModel citizenEventErrorCommandModel,
            CitizenStatusErrorRecordService citizenStatusErrorRecordService) {
        this.citizenEventErrorCommandModel = citizenEventErrorCommandModel;
        this.citizenStatusErrorRecordService = citizenStatusErrorRecordService;
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

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSSXXXXX");

            CitizenStatusErrorRecord citizenStatusErrorRecord = new CitizenStatusErrorRecord();

            citizenStatusErrorRecordService.saveCitizenStatusErrorRecordService(citizenStatusErrorRecord);

            return true;

        } catch (Exception e) {

            if (citizenEventError != null) {
                logger.error(e.getMessage(), e);
            }

            throw e;
        }

    }
    @Autowired
    public void setCitizenStatusErrorRecordService(
            CitizenStatusErrorRecordService citizenStatusErrorRecordService) {
        this.citizenStatusErrorRecordService = citizenStatusErrorRecordService;
    }

}
