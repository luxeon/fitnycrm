package com.fittrackcrm.core.client.repository;

import com.fittrackcrm.core.client.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByTenantIdAndEmail(UUID tenantId, String email);
}
