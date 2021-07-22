package it.gov.pagopa.bpd.citizen_event_error_manager.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen_event_error_manager.command.SubmitFlaggedRecordsCommand;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * See {@link BpdCitizenEventErrorManagerController}
 */
@RestController
class BpdCitizenEventErrorManagerControllerImpl
        extends StatelessController implements BpdCitizenEventErrorManagerController {

    private final BeanFactory beanFactory;

    @Autowired
    BpdCitizenEventErrorManagerControllerImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void resubmitPendingEvents() throws Exception {
        SubmitFlaggedRecordsCommand command =
                beanFactory.getBean(SubmitFlaggedRecordsCommand.class);
        command.execute();
    }

}
