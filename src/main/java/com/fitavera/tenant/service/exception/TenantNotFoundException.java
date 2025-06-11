package com.fitavera.tenant.service.exception;

import java.util.UUID;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(UUID id) {
        super("Tenant not found with id: " + id);
    }
} 