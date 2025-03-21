package com.fittrackcrm.core.tenant.exception;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(Long id) {
        super("Tenant not found with id: " + id);
    }
} 