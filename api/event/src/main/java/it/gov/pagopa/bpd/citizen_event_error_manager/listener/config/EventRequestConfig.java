package it.gov.pagopa.bpd.citizen_event_error_manager.listener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the OnTransactionSaveRequestListener class
 */

@Configuration
@PropertySource("classpath:config/citizenStatusRequestListener.properties")
public class EventRequestConfig {
}
