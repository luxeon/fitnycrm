package com.fitnycrm.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.fitnycrm.tenant.repository.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fitnycrm.user.repository.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmationToken(String token);

    @Query("""
            FROM User user JOIN user.tenants tenant WHERE user.id = :id AND tenant.id = :tenantId
            """)
    Optional<User> findByIdAndTenant(UUID id, UUID tenantId);

    @Query("""
            FROM User user JOIN user.tenants tenant WHERE tenant.id = :tenantId
            """)
    Page<User> findByTenantId(UUID tenantId, Pageable pageable);
} 