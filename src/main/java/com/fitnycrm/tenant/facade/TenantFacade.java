package com.fitnycrm.tenant.facade;

import com.fitnycrm.user.service.model.AuthenticatedUserDetails;
import com.fitnycrm.tenant.exception.TenantAlreadyCreatedException;
import com.fitnycrm.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.tenant.rest.model.UpdateTenantRequest;
import org.springframework.stereotype.Component;

import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.tenant.facade.mapper.TenantMapper;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.tenant.repository.entity.Tenant;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantFacade {

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    public TenantDetailsResponse getById(UUID id) {
        return tenantMapper.toDetailsResponse(tenantService.getById(id));
    }

    public TenantDetailsResponse create(AuthenticatedUserDetails user, CreateTenantRequest request) {
        if (user.getTenantId() != null) {
            throw new TenantAlreadyCreatedException();
        }
        Tenant tenant = tenantMapper.toEntity(request);
        tenant = tenantService.create(user.getId(), tenant);
        return tenantMapper.toDetailsResponse(tenant);
    }

    public TenantDetailsResponse update(UUID id, UpdateTenantRequest request) {
        Tenant tenant = tenantService.update(id, request);
        return tenantMapper.toDetailsResponse(tenant);
    }
} 