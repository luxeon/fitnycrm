package com.fitavera.location.service.mapper;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.location.rest.model.CreateLocationRequest;
import com.fitavera.location.rest.model.UpdateLocationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Location toLocation(CreateLocationRequest request);

    void update(@MappingTarget Location location, UpdateLocationRequest request);
}