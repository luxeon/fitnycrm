package com.fitavera.payment.service.exception;

import java.util.UUID;

public class ClientPaymentAlreadyCancelledException extends RuntimeException {
    public ClientPaymentAlreadyCancelledException(UUID paymentId) {
        super("Client payment already cancelled: " + paymentId);
    }
}
