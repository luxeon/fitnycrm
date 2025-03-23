package com.fitnycrm.designer.client.rest;

import com.fitnycrm.designer.client.service.exception.ClientEmailAlreadyExistsException;
import com.fitnycrm.designer.common.rest.model.ErrorResponse;
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

}
