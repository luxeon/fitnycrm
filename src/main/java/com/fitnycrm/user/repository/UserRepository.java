package com.fitnycrm.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmationToken(String token);

    @Query("""
            FROM User user JOIN user.tenants tenant JOIN user.roles role WHERE user.id = :clientId AND tenant.id = :tenantId 
                        AND role.name = :roleName
            """)
    Optional<User> findByIdAndTenantAndRole(UUID tenantId, UserRole.Name roleName, UUID clientId);

    @Query("SELECT u FROM User u JOIN u.tenants t JOIN u.roles r WHERE t.id = :tenantId AND r.name = :roleName")
    Page<User> findByTenantIdAndRole(UUID tenantId, UserRole.Name roleName, Pageable pageable);
} 