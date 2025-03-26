package com.fitnycrm.user.service.client.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.client.model.CreateClientRequest;
import com.fitnycrm.user.rest.client.model.UpdateClientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ClientRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UUID tenantId, CreateClientRequest request);

    User update(@MappingTarget User user, UpdateClientRequest request);
}
