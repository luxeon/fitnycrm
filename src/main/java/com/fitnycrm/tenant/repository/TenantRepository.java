package com.fitnycrm.tenant.repository;

import com.fitnycrm.tenant.repository.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    
    @Query("SELECT t FROM Tenant t JOIN t.users u WHERE u.id = :userId")
    List<Tenant> findByUserId(UUID userId);
} 