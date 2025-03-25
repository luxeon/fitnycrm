package com.fitnycrm.location.service.exception;

import java.util.UUID;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(UUID id) {
        super("Location not found with id: " + id);
    }
} 