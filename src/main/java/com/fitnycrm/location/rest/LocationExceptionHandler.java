package com.fitnycrm.location.rest;

import com.fitnycrm.client.service.exception.ClientNotFoundException;
import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.location.service.exception.LocationNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocationExceptionHandler {

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleLocationNotFoundException(LocationNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

}
