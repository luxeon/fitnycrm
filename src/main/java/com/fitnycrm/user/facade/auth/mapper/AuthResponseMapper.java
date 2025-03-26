package com.fitnycrm.user.facade.auth.mapper;

import com.fitnycrm.user.rest.model.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthResponseMapper {

    AuthResponse toAuthResponse(String accessToken);

}
