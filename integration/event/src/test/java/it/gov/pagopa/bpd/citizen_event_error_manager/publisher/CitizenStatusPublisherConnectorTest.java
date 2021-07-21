package it.gov.pagopa.bpd.citizen_event_error_manager.publisher;

import eu.sia.meda.event.BaseEventConnectorTest;
import eu.sia.meda.util.TestUtils;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * Test class for the PointTransactionPublisherConnector class
 */

@Import({CitizenStatusPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testCitizenStatusPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.CitizenStatusPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}",
                "connectors.eventConfigurations.items.CitizenStatusPublisherConnector.enable=true"
        })
public class CitizenStatusPublisherConnectorTest extends
        BaseEventConnectorTest<CitizenStatusData, Boolean, CitizenStatusData, Void, CitizenStatusPublisherConnector> {

    @Value("${connectors.eventConfigurations.items.CitizenStatusPublisherConnector.topic}")
    private String topic;

    @Autowired
    private CitizenStatusPublisherConnector citizenStatusPublisherConnector;

    @Override
    protected CitizenStatusPublisherConnector getEventConnector() {
        return citizenStatusPublisherConnector;
    }

    @Override
    protected CitizenStatusData getRequestObject() {
        return TestUtils.mockInstance(new CitizenStatusData());
    }

    @Override
    protected String getTopic() {
        return topic;
    }
}
