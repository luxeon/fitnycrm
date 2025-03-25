package com.fitnycrm.client.service.exception;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(UUID clientId) {
        super("Client with ID " + clientId + " not found");
    }
} 