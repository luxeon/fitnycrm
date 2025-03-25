package com.fitnycrm.client.facade.mapper;

import com.fitnycrm.client.repository.entity.Client;
import com.fitnycrm.client.rest.model.ClientDetailsResponse;
import com.fitnycrm.client.rest.model.ClientSignupRequest;
import com.fitnycrm.client.rest.model.ClientUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toEntity(UUID tenantId, ClientSignupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toEntity(UUID tenantId, ClientUpdateRequest request);

    ClientDetailsResponse toResponse(Client client);
} 