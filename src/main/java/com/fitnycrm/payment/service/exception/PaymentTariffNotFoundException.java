package com.fitnycrm.payment.service.exception;

import java.util.UUID;

public class PaymentTariffNotFoundException extends RuntimeException {

    public PaymentTariffNotFoundException(UUID tariffId) {
        super("Payment tariff '" + tariffId + "' not found");
    }
}
