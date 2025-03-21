package com.fitonyashka.core.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitonyashka.core.tenant.repository.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
} 