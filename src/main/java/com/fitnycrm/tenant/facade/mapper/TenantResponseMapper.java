package com.fitnycrm.tenant.facade.mapper;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.tenant.rest.model.TenantListItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantResponseMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TenantDetailsResponse toDetailsResponse(Tenant tenant);

    @Mapping(target = "createdAt", source = "createdAt")
    TenantListItemResponse toListResponse(Tenant tenant);

} 