package com.fitnycrm.location.facade.mapper;

import com.fitnycrm.location.rest.model.LocationPageItemResponse;
import com.fitnycrm.location.rest.model.LocationRequest;
import com.fitnycrm.location.rest.model.LocationDetailsResponse;
import com.fitnycrm.location.repository.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toEntity(UUID tenantId, LocationRequest request);

    LocationDetailsResponse toDetailsResponse(Location location);

    LocationPageItemResponse toPageItemResponse(Location location);
}