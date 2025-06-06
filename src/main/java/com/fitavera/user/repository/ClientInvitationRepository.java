package com.fitavera.user.repository;

import com.fitavera.tenant.repository.entity.Tenant;
import com.fitavera.user.repository.entity.ClientInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientInvitationRepository extends JpaRepository<ClientInvitation, UUID> {

    Optional<ClientInvitation> findByTenantAndEmail(Tenant tenant, String email);

}