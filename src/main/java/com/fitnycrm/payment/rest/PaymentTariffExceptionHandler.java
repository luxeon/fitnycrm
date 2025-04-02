package com.fitnycrm.payment.rest;

import com.fitnycrm.common.rest.model.ErrorResponse;
import com.fitnycrm.payment.service.exception.PaymentTariffNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PaymentTariffExceptionHandler {

    @ExceptionHandler(PaymentTariffNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePaymentTariffNotFoundException(PaymentTariffNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }
}
