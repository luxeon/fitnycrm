package com.fitavera.location.facade;

import com.fitavera.location.facade.mapper.LocationResponseMapper;
import com.fitavera.location.repository.entity.Location;
import com.fitavera.location.rest.model.CreateLocationRequest;
import com.fitavera.location.rest.model.LocationDetailsResponse;
import com.fitavera.location.rest.model.LocationPageItemResponse;
import com.fitavera.location.rest.model.UpdateLocationRequest;
import com.fitavera.location.service.LocationService;
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