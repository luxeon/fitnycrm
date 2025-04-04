package com.fitnycrm.payment.service.exception;

import java.util.UUID;

public class ClientTrainingCreditNotFoundException extends RuntimeException {
    public ClientTrainingCreditNotFoundException(UUID clientId, UUID trainingId) {
        super("Client training credit not found for client %s and training %s".formatted(clientId, trainingId));
    }
} 