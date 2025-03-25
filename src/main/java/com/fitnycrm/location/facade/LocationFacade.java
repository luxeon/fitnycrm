package com.fitnycrm.location.facade;

import com.fitnycrm.location.dto.LocationRequest;
import com.fitnycrm.location.dto.LocationResponse;
import com.fitnycrm.location.entity.Location;
import com.fitnycrm.location.mapper.LocationMapper;
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
} 