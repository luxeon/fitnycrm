package com.fitnycrm.tenant.facade;

import com.fitnycrm.tenant.facade.mapper.TenantResponseMapper;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.tenant.rest.model.TenantListItemResponse;
import com.fitnycrm.tenant.rest.model.UpdateTenantRequest;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantFacade {

    private final TenantService tenantService;
    private final TenantResponseMapper tenantResponseMapper;

    public TenantDetailsResponse getById(UUID id) {
        return tenantResponseMapper.toDetailsResponse(tenantService.findById(id));
    }

    public TenantDetailsResponse create(AuthenticatedUserDetails user, CreateTenantRequest request) {
        Tenant tenant = tenantService.create(user.getId(), request);
        return tenantResponseMapper.toDetailsResponse(tenant);
    }

    public TenantDetailsResponse update(UUID id, UpdateTenantRequest request) {
        Tenant tenant = tenantService.update(id, request);
        return tenantResponseMapper.toDetailsResponse(tenant);
    }

    public List<TenantListItemResponse> getAllForUser(AuthenticatedUserDetails user) {
        return tenantService.findByUserId(user.getId())
                .stream()
                .map(tenantResponseMapper::toListResponse)
                .toList();
    }
} 