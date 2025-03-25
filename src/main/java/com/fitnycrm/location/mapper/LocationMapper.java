package com.fitnycrm.location.mapper;

import com.fitnycrm.location.dto.LocationRequest;
import com.fitnycrm.location.dto.LocationResponse;
import com.fitnycrm.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toEntity(UUID tenantId, LocationRequest request);

    LocationResponse toResponse(Location location);
} 