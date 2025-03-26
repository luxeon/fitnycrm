package com.fitnycrm.user.expression;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantAccessValidator {

    public boolean check(UUID tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        return tenantId.equals(user.getTenantId());
    }

    public boolean check(UUID tenantId, UUID clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        return tenantId.equals(user.getTenantId()) && clientId.equals(user.getId());
    }
} 