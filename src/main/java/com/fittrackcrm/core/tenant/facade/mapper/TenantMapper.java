package com.fittrackcrm.core.tenant.facade.mapper;

import com.fittrackcrm.core.tenant.repository.entity.Tenant;
import com.fittrackcrm.core.tenant.rest.model.CreateTenantRequest;
import com.fittrackcrm.core.tenant.rest.model.UpdateTenantRequest;
import com.fittrackcrm.core.tenant.rest.model.TenantDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TenantDetailsResponse toDetailsResponse(Tenant tenant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tenant toEntity(CreateTenantRequest request);

    void update(@MappingTarget Tenant tenant, UpdateTenantRequest request);
} 