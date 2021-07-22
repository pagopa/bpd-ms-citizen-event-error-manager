package it.gov.pagopa.bpd.citizen_event_error_manager.service;

import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen_event_error_manager.command.ManageCitizenEventErrorCommand;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import it.gov.pagopa.bpd.citizen_event_error_manager.service.transformer.HeaderAwareRequestTransformer;
import it.gov.pagopa.bpd.common.BaseTest;
import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;
import java.time.OffsetDateTime;

public class BpdCitizenStatusDataPublisherServiceImplTest extends BaseTest {


    @Mock
    private CitizenStatusPublisherConnector citizenStatusPublisherConnector;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private BpdCitizenStatusDataPublisherService bpdCitizenStatusDataPublisherService;

    @SpyBean
    private HeaderAwareRequestTransformer<CitizenStatusData> simpleEventRequestTransformerSpy;

    @SpyBean
    private SimpleEventResponseTransformer simpleEventResponseTransformerSpy;

    @Before
    public void initTest() {
        Mockito.reset(citizenStatusPublisherConnector);
        bpdCitizenStatusDataPublisherService =
                new BpdCitizenStatusDataPublisherServiceImpl(
                        citizenStatusPublisherConnector,
                        simpleEventRequestTransformerSpy,
                        simpleEventResponseTransformerSpy);
    }

    @Test
    public void testSave_Ok() {

        try {

            BDDMockito.doReturn(true)
                    .when(citizenStatusPublisherConnector)
                    .doCall(Mockito.eq(getSaveModel()),Mockito.any(),Mockito.any());

            bpdCitizenStatusDataPublisherService
                    .publishBpdCitizenEvent(getSaveModel(), getRecordHeaders());

            BDDMockito.verify(citizenStatusPublisherConnector, Mockito.atLeastOnce())
                    .doCall(Mockito.eq(getSaveModel()),Mockito.any(),Mockito.any(), Mockito.any());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @SneakyThrows
    @Test
    public void testSave_KO_Connector() {

        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(citizenStatusPublisherConnector)
                .doCall(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());

        expectedException.expect(Exception.class);
        bpdCitizenStatusDataPublisherService.publishBpdCitizenEvent(null, null);

        BDDMockito.verify(citizenStatusPublisherConnector, Mockito.atLeastOnce())
                .doCall(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any());
    }

    protected CitizenStatusData getSaveModel() {
        return CitizenStatusData.builder()
                .fiscalCode("fiscalCode")
                .enabled(false)
                .applyTo("all")
                .updateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .build();
    }

    private RecordHeaders getRecordHeaders() {
        return new RecordHeaders();
    }


}