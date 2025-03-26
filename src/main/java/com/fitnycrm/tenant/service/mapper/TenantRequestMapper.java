package com.fitnycrm.tenant.service.mapper;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.tenant.rest.model.UpdateTenantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tenant toEntity(CreateTenantRequest request);

    void update(@MappingTarget Tenant tenant, UpdateTenantRequest request);
} 