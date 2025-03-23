package com.fitnycrm.designer.user.facade.mapper;

import com.fitnycrm.designer.user.rest.model.UserDetailsResponse;
import com.fitnycrm.designer.user.rest.model.UserSignupRequest;
import com.fitnycrm.designer.user.rest.model.AuthResponse;
import com.fitnycrm.designer.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthResponse toAuthResponse(String accessToken);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserSignupRequest request);

    UserDetailsResponse toAdminDetailsResponse(User user);
}
