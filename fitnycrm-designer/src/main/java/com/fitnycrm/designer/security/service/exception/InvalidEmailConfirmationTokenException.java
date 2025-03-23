package com.fitnycrm.designer.security.service.exception;

public class InvalidEmailConfirmationTokenException extends RuntimeException {
    public InvalidEmailConfirmationTokenException(String message) {
        super(message);
    }
} 