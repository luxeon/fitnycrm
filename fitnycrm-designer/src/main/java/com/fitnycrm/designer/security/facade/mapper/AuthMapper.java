package com.fitnycrm.designer.security.facade.mapper;

import com.fitnycrm.designer.security.rest.model.AdminDetailsResponse;
import com.fitnycrm.designer.security.rest.model.AdminSignupRequest;
import com.fitnycrm.designer.security.rest.model.AuthResponse;
import com.fitnycrm.designer.user.repository.entity.User;
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
