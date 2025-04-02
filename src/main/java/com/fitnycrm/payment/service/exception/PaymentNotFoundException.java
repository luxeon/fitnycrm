package com.fitnycrm.payment.service.exception;

import java.util.UUID;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(UUID paymentId) {
        super("Payment '" + paymentId + "' not found");
    }
} 