package com.fittrackcrm.core.user.repository;

import com.fittrackcrm.core.user.repository.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByName(UserRole.Name name);
} 