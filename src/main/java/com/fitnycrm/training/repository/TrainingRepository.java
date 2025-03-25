package com.fitnycrm.training.repository;

import com.fitnycrm.training.entity.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {
    Optional<Training> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<Training> findByTenantId(UUID tenantId, Pageable pageable);
} 