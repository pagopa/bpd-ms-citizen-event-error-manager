package it.gov.pagopa.bpd.citizen_event_error_manager.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.event.service.ErrorPublisherService;
import eu.sia.meda.eventlistener.BaseEventListenerTest;
import it.gov.pagopa.bpd.citizen_event_error_manager.listener.factory.CitizenStatusCommandModelFactory;
import it.gov.pagopa.bpd.citizen_event_error_manager.command.ManageCitizenEventErrorCommand;
import it.gov.pagopa.bpd.citizen_event_error_manager.model.CitizenEventError;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import java.time.OffsetDateTime;


/**
 * Test class for the OnCitizenStatusErrorRequestListener method
 */

@Import({OnCitizenStatusErrorRequestListener.class})
@TestPropertySource(
        locations = "classpath:config/testCitizenStatusRequestListener.properties",
        properties = {
                "listeners.eventConfigurations.items.OnCitizenStatusErrorRequestListener.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class OnCitizenStatusErrorRequestListenerTest extends BaseEventListenerTest {


    @SpyBean
    ObjectMapper objectMapperSpy;
    @SpyBean
    OnCitizenStatusErrorRequestListener onCitizenStatusErrorRequestListenerSpy;
    @SpyBean
    CitizenStatusCommandModelFactory citizenStatusCommandModelFactorySpy;
    @MockBean
    ManageCitizenEventErrorCommand manageCitizenEventErrorCommand;

    @Value("${listeners.eventConfigurations.items.OnCitizenStatusErrorRequestListener.topic}")
    private String topic;

    @Before
    public void setUp() throws Exception {

        Mockito.reset(
                onCitizenStatusErrorRequestListenerSpy,
                citizenStatusCommandModelFactorySpy,
                manageCitizenEventErrorCommand);
        Mockito.doReturn(true).when(manageCitizenEventErrorCommand).execute();

    }

    @Override
    protected Object getRequestObject() {
        return CitizenEventError.builder()
                .fiscalCode("fiscalCode")
                .origin("winning_transaction")
                .exceptionMessage("message")
                .enabled(false)
                .updateDateTime(OffsetDateTime.parse("2021-01-01T00:00:00.000Z"))
                .build();
    }

    @Override
    protected String getTopic() {
        return topic;
    }

    @Override
    protected void verifyInvocation(String json) {
        try {
            BDDMockito.verify(citizenStatusCommandModelFactorySpy, Mockito.atLeastOnce())
                    .createModel(Mockito.any());
            BDDMockito.verify(objectMapperSpy, Mockito.atLeastOnce())
                    .readValue(Mockito.anyString(), Mockito.eq(CitizenEventError.class));
            BDDMockito.verify(manageCitizenEventErrorCommand, Mockito.atLeastOnce()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Override
    protected ErrorPublisherService getErrorPublisherService() {
        return null;
    }

}