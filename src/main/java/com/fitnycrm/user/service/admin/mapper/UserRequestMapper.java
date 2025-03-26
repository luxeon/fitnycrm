package com.fitnycrm.user.service.admin.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.model.CreateAdminRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(CreateAdminRequest request);

}
