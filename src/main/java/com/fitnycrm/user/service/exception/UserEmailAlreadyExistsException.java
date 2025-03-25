package com.fitnycrm.user.service.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
    }
} 