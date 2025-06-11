package com.fitavera.user.facade.auth.mapper;

import com.fitavera.user.rest.model.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthResponseMapper {

    AuthResponse toAuthResponse(String accessToken);

}
