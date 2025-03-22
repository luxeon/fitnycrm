package com.fittrackcrm.core.tenant.service;

import com.fittrackcrm.core.tenant.exception.TenantAlreadyCreatedException;
import com.fittrackcrm.core.tenant.exception.TenantNotFoundException;
import com.fittrackcrm.core.tenant.repository.TenantRepository;
import com.fittrackcrm.core.tenant.repository.entity.Tenant;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Tenant getById(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(id));
    }

    @Transactional
    public Tenant create(UUID userId, Tenant tenant) {
        User user = userService.findById(userId);
        Tenant savedTenant = tenantRepository.save(tenant);
        user.setTenantId(savedTenant.getId());
        userService.save(user);
        return savedTenant;
    }
} 