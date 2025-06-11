package com.fitavera.visit.service.exception;

import java.util.UUID;

public class VisitNotFoundException extends RuntimeException {

    public VisitNotFoundException(UUID id) {
        super(String.format("Visit with id %s not found", id));
    }
}
