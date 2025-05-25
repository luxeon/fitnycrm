package com.fitavera.user.rest.client;

import com.fitavera.common.rest.model.ErrorResponse;
import com.fitavera.user.service.client.exception.TenantAlreadyContainsUserException;
import com.fitavera.user.service.exception.InvitationExpiredException;
import com.fitavera.user.service.exception.InvitationNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientExceptionHandler {

    @ExceptionHandler(InvitationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvitationNotFoundException(InvitationNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvitationExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvitationExpiredException(InvitationExpiredException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(TenantAlreadyContainsUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleTenantAlreadyContainsUserException(TenantAlreadyContainsUserException e) {
        return ErrorResponse.of(e.getMessage());
    }
} 