package com.fitnycrm.payment.service.exception;

import java.util.UUID;

public class ClientPaymentNotFoundException extends RuntimeException {

    public ClientPaymentNotFoundException(UUID paymentId) {
        super("Payment '" + paymentId + "' not found");
    }
} 