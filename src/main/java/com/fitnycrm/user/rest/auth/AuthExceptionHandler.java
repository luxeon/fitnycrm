package com.fitnycrm.user.rest.auth;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.user.service.auth.exception.InvalidCredentialsException;
import com.fitnycrm.user.service.exception.InvalidEmailConfirmationTokenException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return ErrorResponse.of("Access denied");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidEmailConfirmationTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmailConfirmationTokenException(InvalidEmailConfirmationTokenException e) {
        return ErrorResponse.of(e.getMessage());
    }
}
