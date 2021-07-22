package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.CitizenStatusErrorRecordDAO;
import it.gov.pagopa.bpd.citizen_event_error_manager.connector.jpa.model.CitizenStatusErrorRecord;
import it.gov.pagopa.bpd.common.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;

public class CitizenStatusErrorRecordServiceImplTest extends BaseTest {

    @Mock
    private CitizenStatusErrorRecordDAO citizenStatusErrorRecordDAO;

    private CitizenStatusErrorRecordService citizenStatusErrorRecordService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(citizenStatusErrorRecordDAO);
        citizenStatusErrorRecordService = new CitizenStatusErrorRecordServiceImpl(citizenStatusErrorRecordDAO);
    }

    @Test
    public void getList_OK() {
        BDDMockito.doReturn(Collections.emptyList())
                .when(citizenStatusErrorRecordDAO).findByAvailableForResubmit(Mockito.eq(true));
        List<CitizenStatusErrorRecord> transactionRecordServiceList =
                citizenStatusErrorRecordService.findRecordsToResubmit();
        Assert.assertNotNull(transactionRecordServiceList);
        BDDMockito.verify(citizenStatusErrorRecordDAO).findByAvailableForResubmit(Mockito.eq(true));
    }

    @Test
    public void getList_KO() {
        BDDMockito.when(citizenStatusErrorRecordDAO.findByAvailableForResubmit(Mockito.eq(true)))
                .thenAnswer(invocation -> {
                    throw new Exception();
                });
        expectedException.expect(Exception.class);
        citizenStatusErrorRecordService.findRecordsToResubmit();
        BDDMockito.verify(citizenStatusErrorRecordDAO).findByAvailableForResubmit(Mockito.eq(true));
    }

    @Test
    public void save_OK() {
        CitizenStatusErrorRecord citizenStatusErrorRecord = CitizenStatusErrorRecord.builder().build();
        BDDMockito.doReturn(citizenStatusErrorRecord)
                .when(citizenStatusErrorRecordDAO)
                .save(Mockito.eq(citizenStatusErrorRecord));
        citizenStatusErrorRecord = citizenStatusErrorRecordService
                .saveCitizenStatusErrorRecordService(citizenStatusErrorRecord);
        Assert.assertNotNull(citizenStatusErrorRecord);
        BDDMockito.verify(citizenStatusErrorRecordDAO).save(Mockito.eq(citizenStatusErrorRecord));
    }

    @Test
    public void save_KO() {
        BDDMockito.when(citizenStatusErrorRecordDAO.save(Mockito.any())).thenAnswer(
                invocation -> {
                    throw new Exception();
                });
        expectedException.expect(Exception.class);
        citizenStatusErrorRecordService.saveCitizenStatusErrorRecordService(
                CitizenStatusErrorRecord.builder().build());
        BDDMockito.verify(citizenStatusErrorRecordDAO).save(Mockito.any());
    }

}