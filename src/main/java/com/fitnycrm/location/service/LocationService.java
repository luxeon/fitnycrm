package com.fitnycrm.location.service;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public Location create(Location location) {
        return locationRepository.save(location);
    }
} 