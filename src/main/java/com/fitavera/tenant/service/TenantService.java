package com.fitavera.tenant.service;

import com.fitavera.tenant.repository.TenantRepository;
import com.fitavera.tenant.repository.entity.Tenant;
import com.fitavera.tenant.rest.model.CreateTenantRequest;
import com.fitavera.tenant.rest.model.UpdateTenantRequest;
import com.fitavera.tenant.service.exception.TenantAlreadyCreatedException;
import com.fitavera.tenant.service.exception.TenantNotFoundException;
import com.fitavera.tenant.service.mapper.TenantRequestMapper;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final AdminUserService adminUserService;
    private final TenantRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public Tenant findById(UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(id));
    }

    @Transactional
    public Tenant create(UUID userId, CreateTenantRequest request) {
        User user = adminUserService.findById(userId);
        if (!user.getTenants().isEmpty()) {
            throw new TenantAlreadyCreatedException();
        }
        Tenant tenant = requestMapper.toEntity(request);
        tenant.getUsers().add(user);
        return tenantRepository.save(tenant);
    }

    @Transactional
    public Tenant update(UUID id, UpdateTenantRequest request) {
        Tenant tenant = findById(id);
        requestMapper.update(tenant, request);
        return tenantRepository.save(tenant);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findByUserId(UUID userId) {
        return tenantRepository.findByUserId(userId);
    }
} 