package com.fittrackcrm.core.user.service.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String email) {
        super("Admin with email " + email + " already exists");
    }
} 