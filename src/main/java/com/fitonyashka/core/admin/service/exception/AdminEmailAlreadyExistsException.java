package com.fitonyashka.core.admin.service.exception;

public class AdminEmailAlreadyExistsException extends RuntimeException {
    public AdminEmailAlreadyExistsException(String email) {
        super("Admin with email " + email + " already exists");
    }
} 