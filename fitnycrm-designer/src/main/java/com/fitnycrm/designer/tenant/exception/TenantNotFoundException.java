package com.fitnycrm.designer.tenant.exception;

import java.util.UUID;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(UUID id) {
        super("Tenant not found with id: " + id);
    }
} 