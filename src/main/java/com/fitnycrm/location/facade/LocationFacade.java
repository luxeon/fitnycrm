package com.fitnycrm.location.facade;

import com.fitnycrm.location.rest.model.LocationDetailsResponse;
import com.fitnycrm.location.rest.model.LocationPageItemResponse;
import com.fitnycrm.location.rest.model.LocationRequest;
import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.facade.mapper.LocationMapper;
import com.fitnycrm.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocationFacade {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public Page<LocationPageItemResponse> findAll(UUID tenantId, Pageable pageable) {
        return locationService.findAll(tenantId, pageable)
                .map(locationMapper::toPageItemResponse);
    }

    public LocationDetailsResponse create(UUID tenantId, LocationRequest request) {
        Location location = locationMapper.toEntity(tenantId, request);
        Location savedLocation = locationService.create(location);
        return locationMapper.toDetailsResponse(savedLocation);
    }

    public LocationDetailsResponse update(UUID tenantId, UUID id, LocationRequest request) {
        Location location = locationMapper.toEntity(tenantId, request);
        Location updatedLocation = locationService.update(id, location);
        return locationMapper.toDetailsResponse(updatedLocation);
    }

    public void delete(UUID tenantId, UUID id) {
        locationService.delete(tenantId, id);
    }

    public LocationDetailsResponse findById(UUID tenantId, UUID id) {
        Location location = locationService.findById(tenantId, id);
        return locationMapper.toDetailsResponse(location);
    }
} 