package com.fittrackcrm.core.client.facade.mapper;

import com.fittrackcrm.core.client.repository.entity.Client;
import com.fittrackcrm.core.client.rest.model.ClientDetailsResponse;
import com.fittrackcrm.core.client.rest.model.ClientSignupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toEntity(UUID tenantId, ClientSignupRequest request);

    ClientDetailsResponse toResponse(Client client);
} 