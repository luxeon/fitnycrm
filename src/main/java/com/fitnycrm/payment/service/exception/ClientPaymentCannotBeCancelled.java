package com.fitnycrm.payment.service.exception;

public class ClientPaymentCannotBeCancelled extends RuntimeException {

    public ClientPaymentCannotBeCancelled(String message) {
        super(message);
    }
}
