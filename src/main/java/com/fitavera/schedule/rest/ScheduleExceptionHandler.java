package com.fitavera.schedule.rest;

import com.fitavera.common.rest.model.ErrorResponse;
import com.fitavera.schedule.service.exception.ScheduleNotFoundException;
import com.fitavera.schedule.service.exception.ScheduleTenantMismatchException;
import com.fitavera.schedule.service.exception.ScheduleTrainingMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScheduleExceptionHandler {

    @ExceptionHandler(ScheduleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleScheduleNotFoundException(ScheduleNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ScheduleTrainingMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleScheduleTrainingMismatchException(ScheduleTrainingMismatchException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ExceptionHandler(ScheduleTenantMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleScheduleTenantMismatchException(ScheduleTenantMismatchException e) {
        return ErrorResponse.of(e.getMessage());
    }
} 