package com.fitnycrm.user.service.exception;

public class InvalidEmailConfirmationTokenException extends RuntimeException {
    public InvalidEmailConfirmationTokenException(String message) {
        super(message);
    }
} 