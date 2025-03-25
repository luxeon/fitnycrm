package com.fitnycrm.client.rest;

import com.fitnycrm.client.service.exception.ClientEmailAlreadyExistsException;
import com.fitnycrm.client.service.exception.ClientNotFoundException;
import com.fitnycrm.common.rest.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientExceptionHandler {

    @ExceptionHandler(ClientEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleClientEmailAlreadyExistsException(ClientEmailAlreadyExistsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleClientNotFoundException(ClientNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

}
