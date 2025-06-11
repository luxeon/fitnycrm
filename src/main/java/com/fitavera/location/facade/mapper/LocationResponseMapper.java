package com.fitavera.location.facade.mapper;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.location.rest.model.LocationDetailsResponse;
import com.fitavera.location.rest.model.LocationPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationResponseMapper {

    LocationDetailsResponse toDetailsResponse(Location location);

    LocationPageItemResponse toPageItemResponse(Location location);
}