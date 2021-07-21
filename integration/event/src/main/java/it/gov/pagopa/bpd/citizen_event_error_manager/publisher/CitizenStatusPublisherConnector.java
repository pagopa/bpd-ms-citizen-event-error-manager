package it.gov.pagopa.bpd.citizen_event_error_manager.publisher;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import it.gov.pagopa.bpd.citizen_event_error_manager.publisher.model.CitizenStatusData;
import org.springframework.stereotype.Service;

@Service
public class CitizenStatusPublisherConnector extends BaseEventConnector<CitizenStatusData, Boolean, CitizenStatusData, Void> {

    /**
     * @param statusUpdate        StatusUpdate instance to be used as message content
     * @param requestTransformer  Transformer for the request data
     * @param responseTransformer Transformer for the call response
     * @param args                Additional args to be used in the call
     * @return Exit status for the call
     */
    public Boolean doCall(
            CitizenStatusData statusUpdate, IEventRequestTransformer<CitizenStatusData, CitizenStatusData> requestTransformer,
            IEventResponseTransformer<Void, Boolean> responseTransformer,
            Object... args) throws Exception {
        return this.call(statusUpdate, requestTransformer, responseTransformer, args);
    }
}