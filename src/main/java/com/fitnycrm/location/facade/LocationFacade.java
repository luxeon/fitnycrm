package com.fitnycrm.location.facade;

import com.fitnycrm.location.rest.model.LocationRequest;
import com.fitnycrm.location.rest.model.LocationResponse;
import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.facade.mapper.LocationMapper;
import com.fitnycrm.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocationFacade {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationResponse create(UUID tenantId, LocationRequest request) {
        Location location = locationMapper.toEntity(tenantId, request);
        Location savedLocation = locationService.create(location);
        return locationMapper.toResponse(savedLocation);
    }

    public LocationResponse update(UUID tenantId, UUID id, LocationRequest request) {
        Location location = locationMapper.toEntity(tenantId, request);
        Location updatedLocation = locationService.update(id, location);
        return locationMapper.toResponse(updatedLocation);
    }
} 