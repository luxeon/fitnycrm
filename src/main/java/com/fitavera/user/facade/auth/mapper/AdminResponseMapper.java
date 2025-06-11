package com.fitavera.user.facade.auth.mapper;

import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.model.AdminDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminResponseMapper {

    AdminDetailsResponse toDetailsResponse(User user);

}
