package com.fitnycrm.designer.user.service.exception;

public class InvalidEmailConfirmationTokenException extends RuntimeException {
    public InvalidEmailConfirmationTokenException(String message) {
        super(message);
    }
} 