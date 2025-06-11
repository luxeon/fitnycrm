package com.fitavera.user.facade.client;

import com.fitavera.user.facade.client.mapper.ClientResponseMapper;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.client.model.*;
import com.fitavera.user.service.auth.model.AuthenticatedUserDetails;
import com.fitavera.user.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientService clientService;
    private final ClientResponseMapper responseMapper;

    public void invite(UUID tenantId, InviteClientRequest request, AuthenticatedUserDetails user) {
        clientService.invite(tenantId, request.email(), user.getId());
    }

    public ClientDetailsResponse update(UUID tenantId, UUID clientId, UpdateClientRequest request) {
        return responseMapper.toDetailsResponse(
                clientService.update(
                        tenantId,
                        clientId,
                        request
                )
        );
    }

    public ClientDetailsResponse findById(UUID tenantId, UUID clientId) {
        return responseMapper.toDetailsResponse(
                clientService.findById(tenantId, clientId)
        );
    }

    public Page<ClientPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return clientService.findByTenantId(tenantId, pageable)
                .map(responseMapper::toPageItemResponse);
    }

    public ClientDetailsResponse signup(UUID tenantId, UUID clientInvitationId, SignupClientRequest request) {
        User client = clientService.signup(tenantId, clientInvitationId, request);
        return responseMapper.toDetailsResponse(client);
    }

    public void joinByInvitation(UUID tenantId, UUID clientInvitationId, AuthenticatedUserDetails user) {
        clientService.joinByInvitation(tenantId, clientInvitationId, user.getId());
    }
} 