package com.fitnycrm.designer.common.rest;

import com.fitnycrm.designer.client.service.exception.ClientEmailAlreadyExistsException;
import com.fitnycrm.designer.common.rest.model.ErrorResponse;
import com.fitnycrm.designer.common.rest.model.ValidationError;
import com.fitnycrm.designer.security.service.exception.InvalidCredentialsException;
import com.fitnycrm.designer.security.service.exception.InvalidEmailConfirmationTokenException;
import com.fitnycrm.designer.tenant.exception.TenantAlreadyCreatedException;
import com.fitnycrm.designer.tenant.exception.TenantNotFoundException;
import com.fitnycrm.designer.user.service.exception.RoleNotFoundException;
import com.fitnycrm.designer.user.service.exception.UserEmailAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTenantNotFoundException(TenantNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ClientEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleClientEmailAlreadyExistsException(ClientEmailAlreadyExistsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler({RoleNotFoundException.class, TenantAlreadyCreatedException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleAdminRoleNotFoundException(RuntimeException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(InvalidEmailConfirmationTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmailConfirmationTokenException(InvalidEmailConfirmationTokenException e) {
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

        return ErrorResponse.of("Validation failed", validationErrors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        List<ValidationError> validationErrors = e.getAllValidationResults()
                .stream()
                .map(error -> new ValidationError(
                        error.getMethodParameter().getParameterName(),
                        error.getResolvableErrors().getFirst().getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return ErrorResponse.of("Validation failed", validationErrors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return ErrorResponse.of("Access denied");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(e.getMessage());
    }
} 