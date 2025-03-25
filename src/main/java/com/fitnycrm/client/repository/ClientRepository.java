package com.fitnycrm.client.repository;

import com.fitnycrm.client.repository.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByTenantIdAndEmail(UUID tenantId, String email);

    Page<Client> findByTenantId(UUID tenantId, Pageable pageable);
}
