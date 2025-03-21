package com.fitonyashka.core.admin.facade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitonyashka.core.admin.repository.entity.Admin;
import com.fitonyashka.core.admin.rest.model.AdminDetailsResponse;
import com.fitonyashka.core.admin.rest.model.AdminSignupRequest;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Admin toEntity(AdminSignupRequest request);

    AdminDetailsResponse toResponse(Admin admin);
} 