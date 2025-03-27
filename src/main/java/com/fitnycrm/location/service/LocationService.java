package com.fitnycrm.location.service;

import com.fitnycrm.location.repository.LocationRepository;
import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.location.rest.model.CreateLocationRequest;
import com.fitnycrm.location.rest.model.UpdateLocationRequest;
import com.fitnycrm.location.service.exception.LocationNotFoundException;
import com.fitnycrm.location.service.mapper.LocationRequestMapper;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
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
    private final LocationRequestMapper requestMapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public Page<Location> findAll(UUID tenantId, Pageable pageable) {
        return locationRepository.findByTenantId(tenantId, pageable);
    }

    @Transactional
    public Location create(UUID tenantId, CreateLocationRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        Location location = requestMapper.toLocation(tenant, request);
        return locationRepository.save(location);
    }

    @Transactional
    public Location update(UUID tenantId, UUID id, UpdateLocationRequest request) {
        Location location = findById(tenantId, id);
        requestMapper.update(location, request);
        return locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public Location findById(UUID tenantId, UUID id) {
        return locationRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        Location location = findById(tenantId, id);
        locationRepository.delete(location);
    }
} 