package com.fitnycrm.designer.tenant.facade;

import com.fitnycrm.designer.user.service.model.AuthenticatedUserDetails;
import com.fitnycrm.designer.tenant.exception.TenantAlreadyCreatedException;
import com.fitnycrm.designer.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.designer.tenant.rest.model.UpdateTenantRequest;
import org.springframework.stereotype.Component;

import com.fitnycrm.designer.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.designer.tenant.facade.mapper.TenantMapper;
import com.fitnycrm.designer.tenant.service.TenantService;
import com.fitnycrm.designer.tenant.repository.entity.Tenant;

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