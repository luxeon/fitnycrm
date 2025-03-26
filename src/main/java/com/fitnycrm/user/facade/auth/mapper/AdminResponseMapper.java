package com.fitnycrm.user.facade.auth.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.model.AdminDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminResponseMapper {

    AdminDetailsResponse toDetailsResponse(User user);

}
