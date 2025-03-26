package com.fitnycrm.tenant.service;

import com.fitnycrm.tenant.exception.TenantNotFoundException;
import com.fitnycrm.tenant.facade.mapper.TenantResponseMapper;
import com.fitnycrm.tenant.repository.TenantRepository;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.tenant.rest.model.UpdateTenantRequest;
import com.fitnycrm.tenant.service.mapper.TenantRequestMapper;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final AdminUserService adminUserService;
    private final TenantResponseMapper tenantResponseMapper;
    private final TenantRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public Tenant getById(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(id));
    }

    @Transactional
    public Tenant create(UUID userId, CreateTenantRequest request) {
        User user = adminUserService.findById(userId);
        Tenant tenant = requestMapper.toEntity(request);
        tenant.getUsers().add(user);
        return tenantRepository.save(tenant);
    }

    @Transactional
    public Tenant update(UUID id, UpdateTenantRequest request) {
        Tenant tenant = getById(id);
        requestMapper.update(tenant, request);
        return tenantRepository.save(tenant);
    }
} 