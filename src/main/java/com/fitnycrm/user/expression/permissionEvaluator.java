package com.fitnycrm.user.expression;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class permissionEvaluator {

    public boolean check(UUID tenantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        return !isEmpty(user.getTenantIds()) && user.getTenantIds().contains(tenantId);
    }

    public boolean check(UUID tenantId, UUID clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        return !isEmpty(user.getTenantIds()) && user.getTenantIds().contains(tenantId) && clientId.equals(user.getId());
    }
} 