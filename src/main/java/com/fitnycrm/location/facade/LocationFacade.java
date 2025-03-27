package com.fitnycrm.location.facade;

import com.fitnycrm.location.rest.model.LocationDetailsResponse;
import com.fitnycrm.location.rest.model.LocationPageItemResponse;
import com.fitnycrm.location.rest.model.CreateLocationRequest;
import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.facade.mapper.LocationResponseMapper;
import com.fitnycrm.location.rest.model.UpdateLocationRequest;
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
    private final LocationResponseMapper responseMapper;

    public Page<LocationPageItemResponse> findAll(UUID tenantId, Pageable pageable) {
        return locationService.findAll(tenantId, pageable)
                .map(responseMapper::toPageItemResponse);
    }

    public LocationDetailsResponse create(UUID tenantId, CreateLocationRequest request) {
        Location savedLocation = locationService.create(tenantId, request);
        return responseMapper.toDetailsResponse(savedLocation);
    }

    public LocationDetailsResponse update(UUID tenantId, UUID id, UpdateLocationRequest request) {
        Location updatedLocation = locationService.update(tenantId, id, request);
        return responseMapper.toDetailsResponse(updatedLocation);
    }

    public void delete(UUID tenantId, UUID id) {
        locationService.delete(tenantId, id);
    }

    public LocationDetailsResponse findById(UUID tenantId, UUID id) {
        Location location = locationService.findById(tenantId, id);
        return responseMapper.toDetailsResponse(location);
    }
} 