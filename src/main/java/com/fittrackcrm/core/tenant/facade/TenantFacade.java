package com.fittrackcrm.core.tenant.facade;

import com.fittrackcrm.core.tenant.rest.model.CreateTenantRequest;
import org.springframework.stereotype.Component;

import com.fittrackcrm.core.tenant.rest.model.TenantDetailsResponse;
import com.fittrackcrm.core.tenant.facade.mapper.TenantMapper;
import com.fittrackcrm.core.tenant.service.TenantService;
import com.fittrackcrm.core.tenant.repository.entity.Tenant;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantFacade {

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    public TenantDetailsResponse getById(String id) {
        return tenantMapper.toDetailsResponse(tenantService.getById(UUID.fromString(id)));
    }

    public TenantDetailsResponse create(CreateTenantRequest request) {
        Tenant tenant = tenantMapper.toEntity(request);
        
        tenant = tenantService.create(tenant);
        
        return tenantMapper.toDetailsResponse(tenant);
    }
} 