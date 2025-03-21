package com.fitonyashka.core.tenant.facade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitonyashka.core.tenant.rest.model.TenantDetailsResponse;
import com.fitonyashka.core.tenant.repository.entity.Tenant;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TenantDetailsResponse toDetailsResponse(Tenant tenant);
} 