package com.fitnycrm.training.service.exception;

import java.util.UUID;

public class TrainingNotFoundException extends RuntimeException {
    public TrainingNotFoundException(UUID id) {
        super("Training not found with id: " + id);
    }
} 