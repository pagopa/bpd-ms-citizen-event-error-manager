package it.gov.pagopa.bpd.citizen_event_error_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/citizenStatus.properties")
public class CitizenStatusDataConfig {
}
