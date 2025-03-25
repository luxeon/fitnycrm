package com.fitnycrm.training.rest;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.training.service.exception.TrainingNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TrainingExceptionHandler {

    @ExceptionHandler(TrainingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTrainingNotFoundException(TrainingNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }
} 