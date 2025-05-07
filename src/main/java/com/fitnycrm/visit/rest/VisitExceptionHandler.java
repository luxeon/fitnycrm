package com.fitnycrm.visit.rest;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.training.service.exception.TrainingNotFoundException;
import com.fitnycrm.visit.service.exception.VisitRegistrationException;
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

}
