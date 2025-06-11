package com.fitavera.payment.service.exception;

public class ClientPaymentCannotBeCancelled extends RuntimeException {

    public ClientPaymentCannotBeCancelled(String message) {
        super(message);
    }
}
