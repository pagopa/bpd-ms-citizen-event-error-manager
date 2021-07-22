package it.gov.pagopa.bpd.citizen_event_error_manager.command;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventErrorCommandModel;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.BpdCitizenStatusDataPublisherService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper.CitizenStatusDataMapper;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper.CitizenStatusErrorRecordMapper;
import it.gov.pagopa.bpd.common.BaseTest;
import junit.framework.TestCase;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.time.OffsetDateTime;

public class ManageCitizenEventErrorCommandImplTest extends BaseTest {

    @Mock
    CitizenStatusErrorRecordService citizenStatusErrorRecordService;

    @Mock
    BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService;

    @Spy
    CitizenStatusDataMapper citizenStatusDataMapper;

    @Spy
    CitizenStatusErrorRecordMapper citizenStatusErrorRecordMapper;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(
                citizenStatusErrorRecordService, bpdCitizenStatusDataPublisherService,
                citizenStatusDataMapper, citizenStatusErrorRecordMapper);
    }

    @Test
    public void TestExecute_OK_Insert_And_Send() {

        CitizenEventError citizenEventError = getRequestObject();
        CitizenStatusErrorRecord citizenStatusErrorRecord = getSavedModel();
        Headers headers = new RecordHeaders();

        ArgumentCaptor<CitizenStatusErrorRecord> argument = ArgumentCaptor.forClass(
                CitizenStatusErrorRecord.class);
        BDDMockito.doReturn(citizenStatusErrorRecord).when(citizenStatusErrorRecordService)
                .saveCitizenStatusErrorRecordService(Mockito.eq(citizenStatusErrorRecord));
        ManageCitizenEventErrorCommandImpl saveTransactionCommand = new ManageCitizenEventErrorCommandImpl(
                CitizenEventErrorCommandModel.builder().payload(citizenEventError).headers(headers).build(),
                citizenStatusErrorRecordService,
                bpdCitizenStatusDataPublisherService,
                citizenStatusErrorRecordMapper,
                citizenStatusDataMapper,
                10,
                "bpd_winning_transaction,bpd_payment_instrument");
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        Mockito.verify(citizenStatusDataMapper).mapTo(Mockito.eq(citizenStatusErrorRecord));
        Mockito.verify(citizenStatusErrorRecordMapper).mapTo(Mockito.eq(citizenEventError));
        Mockito.verify(citizenStatusErrorRecordService).findExistingRecord(
                Mockito.eq(citizenEventError.getFiscalCode()), Mockito.eq(citizenEventError.getOrigin()),
                Mockito.eq(citizenEventError.getEnabled()), Mockito.eq(citizenEventError.getUpdateDateTime()));


    }

    protected CitizenStatusData toSendModel() {
        return CitizenStatusData.builder()
                .applyTo("bpd_winning_transaction")
                .enabled(false)
                .applyTo("bpd_winning_transaction")
                .updateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .fiscalCode("fiscalCode")
                .build();
    }

    protected CitizenStatusErrorRecord getSavedModel() {
        return CitizenStatusErrorRecord.builder()
                .fiscalCode("fiscalCode")
                .status(false)
                .origin("bpd_winning_transaction")
                .numberOfRetries(1)
                .availableForResubmit(true)
                .exceptionMessage("test")
                .updateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .lastResubmitDate(null)
                .build();
    }

    protected CitizenEventError getRequestObject() {
        return CitizenEventError.builder()
                .fiscalCode("fiscalCode")
                .origin("bpd_winning_transaction")
                .exceptionMessage("message")
                .enabled(false)
                .updateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .build();
    }

}