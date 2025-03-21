package com.fittrackcrm.core.tenant.rest;

import org.springframework.web.bind.annotation.RestController;

import com.fittrackcrm.core.tenant.rest.model.TenantDetailsResponse;
import com.fittrackcrm.core.tenant.facade.TenantFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TenantController implements TenantControllerApi {

    private final TenantFacade tenantFacade;

    @Override
    public TenantDetailsResponse getOne(Long id) {
        return tenantFacade.getById(id);
    }
} 