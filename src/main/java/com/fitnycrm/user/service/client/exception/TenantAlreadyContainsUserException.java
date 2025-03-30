package com.fitnycrm.user.service.client.exception;

import java.util.UUID;

public class TenantAlreadyContainsUserException extends RuntimeException {

    public TenantAlreadyContainsUserException(UUID tenantId, UUID userId) {
        super(String.format("Tenant %s already contains user %s", tenantId, userId));
    }
}
