package com.fitnycrm.designer.client.service.exception;

public class ClientEmailAlreadyExistsException extends RuntimeException {
    public ClientEmailAlreadyExistsException(String email) {
        super("Client with email " + email + " already exists");
    }
} 