package com.fittrackcrm.core.user.facade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.rest.model.UserDetailsResponse;
import com.fittrackcrm.core.user.rest.model.UserSignupRequest;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UUID tenantId, UserSignupRequest request);

    UserDetailsResponse toResponse(User user);
} 