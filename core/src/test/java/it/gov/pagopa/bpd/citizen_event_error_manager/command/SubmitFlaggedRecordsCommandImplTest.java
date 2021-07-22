package it.gov.pagopa.bpd.citizen_event_error_manager.command;

import eu.sia.meda.BaseTest;
import eu.sia.meda.async.util.AsyncUtils;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.BpdCitizenStatusDataPublisherService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.CitizenStatusErrorRecordService;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper.CitizenStatusDataMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.MockitoAnnotations.initMocks;

public class SubmitFlaggedRecordsCommandImplTest  extends BaseTest {

    @Mock
    CitizenStatusErrorRecordService citizenStatusErrorRecordService;

    @Mock
    BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService;

    @Spy
    CitizenStatusDataMapper citizenStatusDataMapper;

    @Spy
    AsyncUtils asyncUtilsSpy;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        initMocks(this);
        Mockito.reset(
                citizenStatusErrorRecordService,
                bpdCitizenStatusDataPublisherService,
                citizenStatusDataMapper,
                asyncUtilsSpy);
    }


    @Test
    public void TestExecute_OK() {
        CitizenStatusErrorRecord transactionRecord = getSavedModel();
        BDDMockito.doReturn(Collections.singletonList(transactionRecord)).when(citizenStatusErrorRecordService)
                .findRecordsToResubmit();
        SubmitFlaggedRecordsCommandImpl saveTransactionCommand = new SubmitFlaggedRecordsCommandImpl(
                citizenStatusErrorRecordService,
                bpdCitizenStatusDataPublisherService,
                citizenStatusDataMapper);
        saveTransactionCommand.setAsyncUtils(asyncUtilsSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        BDDMockito.verify(bpdCitizenStatusDataPublisherService)
                .publishBpdCitizenEvent(Mockito.eq(getRequestModel()),Mockito.any());
    }

    protected CitizenStatusData getRequestModel() {
        return CitizenStatusData.builder()
                .applyTo("bpd_winning_transaction")
                .enabled(false)
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

}