package com.fitnycrm.user.facade.client;

import com.fitnycrm.user.facade.client.mapper.ClientResponseMapper;
import com.fitnycrm.user.rest.client.model.ClientDetailsResponse;
import com.fitnycrm.user.rest.client.model.ClientPageItemResponse;
import com.fitnycrm.user.rest.client.model.CreateClientRequest;
import com.fitnycrm.user.rest.client.model.UpdateClientRequest;
import com.fitnycrm.user.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientService clientService;
    private final ClientResponseMapper mapper;

    public ClientDetailsResponse create(UUID tenantId, CreateClientRequest request) {
        return mapper.toDetailsResponse(
                clientService.create(
                        tenantId, request
                )
        );
    }

    public ClientDetailsResponse update(UUID tenantId, UUID clientId, UpdateClientRequest request) {
        return mapper.toDetailsResponse(
                clientService.update(
                        tenantId,
                        clientId,
                        request
                )
        );
    }

    public void delete(UUID tenantId, UUID clientId) {
        clientService.delete(tenantId, clientId);
    }

    public ClientDetailsResponse findById(UUID tenantId, UUID clientId) {
        return mapper.toDetailsResponse(
                clientService.findById(tenantId, clientId)
        );
    }

    public Page<ClientPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return clientService.findByTenantId(tenantId, pageable)
                .map(mapper::toPageItemResponse);
    }
} 