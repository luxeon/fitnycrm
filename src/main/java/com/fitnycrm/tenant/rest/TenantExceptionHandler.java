package com.fitnycrm.tenant.rest;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.tenant.service.exception.TenantAlreadyCreatedException;
import com.fitnycrm.tenant.service.exception.TenantNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantExceptionHandler {

    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTenantNotFoundException(TenantNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(TenantAlreadyCreatedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleTenantAlreadyCreatedException(RuntimeException e) {
        return ErrorResponse.of(e.getMessage());
    }

}
