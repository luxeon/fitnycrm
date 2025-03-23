package com.fitnycrm.designer.tenant.service;

import com.fitnycrm.designer.tenant.exception.TenantNotFoundException;
import com.fitnycrm.designer.tenant.facade.mapper.TenantMapper;
import com.fitnycrm.designer.tenant.repository.TenantRepository;
import com.fitnycrm.designer.tenant.repository.entity.Tenant;
import com.fitnycrm.designer.tenant.rest.model.UpdateTenantRequest;
import com.fitnycrm.designer.user.repository.entity.User;
import com.fitnycrm.designer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserService userService;
    private final TenantMapper tenantMapper;

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

    @Transactional
    public Tenant update(UUID id, UpdateTenantRequest request) {
        Tenant tenant = getById(id);
        tenantMapper.update(tenant, request);
        return tenantRepository.save(tenant);
    }
} 