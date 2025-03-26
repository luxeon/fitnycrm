package com.fitnycrm.user.rest;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.user.service.exception.RoleNotFoundException;
import com.fitnycrm.user.service.exception.UserEmailAlreadyExistsException;
import com.fitnycrm.user.service.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleRoleNotFoundExceptionException(RoleNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }
}
