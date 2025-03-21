package com.fitonyashka.core.tenant.facade;

import org.springframework.stereotype.Component;

import com.fitonyashka.core.tenant.rest.model.TenantDetailsResponse;
import com.fitonyashka.core.tenant.facade.mapper.TenantMapper;
import com.fitonyashka.core.tenant.service.TenantService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantFacade {

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    public TenantDetailsResponse getById(Long id) {
        return tenantMapper.toDetailsResponse(tenantService.getById(id));
    }
} 