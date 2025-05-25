package com.fitavera.user.repository;

import com.fitavera.user.repository.entity.User;
import com.fitavera.user.repository.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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