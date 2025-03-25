package com.fitnycrm.location.service;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.repository.LocationRepository;
import com.fitnycrm.location.service.exception.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public Page<Location> findAll(UUID tenantId, Pageable pageable) {
        return locationRepository.findByTenantId(tenantId, pageable);
    }

    @Transactional
    public Location create(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location update(UUID id, Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        if (!existingLocation.getTenantId().equals(location.getTenantId())) {
            throw new LocationNotFoundException(id);
        }

        existingLocation.setAddress(location.getAddress());
        existingLocation.setCity(location.getCity());
        existingLocation.setState(location.getState());
        existingLocation.setPostalCode(location.getPostalCode());
        existingLocation.setCountry(location.getCountry());
        existingLocation.setTimezone(location.getTimezone());

        return locationRepository.save(existingLocation);
    }

    @Transactional(readOnly = true)
    public Location findById(UUID tenantId, UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        if (!location.getTenantId().equals(tenantId)) {
            throw new LocationNotFoundException(id);
        }
        
        return location;
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        
        if (!location.getTenantId().equals(tenantId)) {
            throw new LocationNotFoundException(id);
        }

        locationRepository.delete(location);
    }
} 