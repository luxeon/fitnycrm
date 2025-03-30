package com.fitnycrm.location.facade.mapper;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.rest.model.LocationDetailsResponse;
import com.fitnycrm.location.rest.model.LocationPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationResponseMapper {

    LocationDetailsResponse toDetailsResponse(Location location);

    LocationPageItemResponse toPageItemResponse(Location location);
}