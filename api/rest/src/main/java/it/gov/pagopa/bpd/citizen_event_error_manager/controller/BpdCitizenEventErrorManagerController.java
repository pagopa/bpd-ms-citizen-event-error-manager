package it.gov.pagopa.bpd.citizen_event_error_manager.controller;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali citizen-event-error-manager Controller")
@RequestMapping("/bpd/citizen-event-error-manager")
public interface BpdCitizenEventErrorManagerController {

    @PostMapping(value = "/resubmitPendingEvents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void resubmitPendingEvents() throws Exception;

}
