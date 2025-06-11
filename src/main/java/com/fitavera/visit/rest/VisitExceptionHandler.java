package com.fitavera.visit.rest;

import com.fitavera.common.rest.model.ErrorResponse;
import com.fitavera.visit.service.exception.VisitCancellationException;
import com.fitavera.visit.service.exception.VisitRegistrationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VisitExceptionHandler {

    @ExceptionHandler(VisitRegistrationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleVisitRegistrationException(VisitRegistrationException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(VisitCancellationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleVisitCancellationException(VisitCancellationException e) {
        return ErrorResponse.of(e.getMessage());
    }

}
