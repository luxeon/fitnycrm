package com.fitavera.schedule.service.exception;

public class ScheduleTenantMismatchException extends RuntimeException {
    public ScheduleTenantMismatchException(String message) {
        super(message);
    }
} 