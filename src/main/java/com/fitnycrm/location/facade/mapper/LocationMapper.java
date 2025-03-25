package com.fitnycrm.location.facade.mapper;

import com.fitnycrm.location.rest.model.LocationRequest;
import com.fitnycrm.location.rest.model.LocationResponse;
import com.fitnycrm.location.repository.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toEntity(UUID tenantId, LocationRequest request);

    LocationResponse toResponse(Location location);
} 