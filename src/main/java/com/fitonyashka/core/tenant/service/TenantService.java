package com.fitonyashka.core.tenant.service;

import com.fitonyashka.core.tenant.exception.TenantNotFoundException;
import com.fitonyashka.core.tenant.repository.TenantRepository;
import com.fitonyashka.core.tenant.repository.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    public Tenant getById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException(id));
    }
} 