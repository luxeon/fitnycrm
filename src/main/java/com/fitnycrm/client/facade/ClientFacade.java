package com.fitnycrm.client.facade;

import com.fitnycrm.client.facade.mapper.ClientMapper;
import com.fitnycrm.client.rest.model.ClientDetailsResponse;
import com.fitnycrm.client.rest.model.ClientPageItemResponse;
import com.fitnycrm.client.rest.model.ClientSignupRequest;
import com.fitnycrm.client.rest.model.ClientUpdateRequest;
import com.fitnycrm.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientService clientService;
    private final ClientMapper mapper;

    public ClientDetailsResponse create(UUID tenantId, ClientSignupRequest request) {
        return mapper.toDetailsResponse(
                clientService.create(
                        mapper.toEntity(tenantId, request)
                )
        );
    }

    public ClientDetailsResponse update(UUID tenantId, UUID clientId, ClientUpdateRequest request) {
        return mapper.toDetailsResponse(
                clientService.update(
                        tenantId,
                        clientId,
                        mapper.toEntity(tenantId, request)
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