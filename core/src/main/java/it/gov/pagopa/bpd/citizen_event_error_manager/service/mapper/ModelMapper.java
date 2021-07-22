package it.gov.pagopa.bpd.citizen_event_error_manager.service.mapper;

/**
 * interface to be used for inheritance for model mapping
 */

public interface ModelMapper<T, U> {

    U mapTo(T entity);

    T mapFrom(U entity);

}
