package com.fittrackcrm.core.common.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fittrackcrm.core.user.service.exception.UserEmailAlreadyExistsException;
import com.fittrackcrm.core.security.service.exception.InvalidCredentialsException;
import com.fittrackcrm.core.common.rest.model.ErrorResponse;
import com.fittrackcrm.core.common.rest.model.ValidationError;
import com.fittrackcrm.core.tenant.exception.TenantNotFoundException;
import com.fittrackcrm.core.user.service.exception.RoleNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTenantNotFoundException(TenantNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAdminEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleAdminRoleNotFoundException(RoleNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        List<ValidationError> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return ErrorResponse.of("Bad Request", validationErrors);
    }
} 