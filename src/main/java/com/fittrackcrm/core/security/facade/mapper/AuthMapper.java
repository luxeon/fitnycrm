package com.fittrackcrm.core.security.facade.mapper;

import com.fittrackcrm.core.security.rest.model.AdminDetailsResponse;
import com.fittrackcrm.core.security.rest.model.AdminSignupRequest;
import com.fittrackcrm.core.security.rest.model.AuthResponse;
import com.fittrackcrm.core.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthResponse toAuthResponse(String accessToken);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(AdminSignupRequest request);

    AdminDetailsResponse toAdminDetailsResponse(User user);
}
