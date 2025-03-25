package com.fitnycrm.client.facade;

import com.fitnycrm.client.facade.mapper.ClientMapper;
import com.fitnycrm.client.rest.model.ClientDetailsResponse;
import com.fitnycrm.client.rest.model.ClientSignupRequest;
import com.fitnycrm.client.rest.model.ClientUpdateRequest;
import com.fitnycrm.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientService clientService;
    private final ClientMapper mapper;

    public ClientDetailsResponse createClient(UUID tenantId, ClientSignupRequest request) {
        return mapper.toResponse(
                clientService.createClient(
                        mapper.toEntity(tenantId, request)
                )
        );
    }

    public ClientDetailsResponse updateClient(UUID tenantId, UUID clientId, ClientUpdateRequest request) {
        return mapper.toResponse(
                clientService.updateClient(
                        tenantId,
                        clientId,
                        mapper.toEntity(tenantId, request)
                )
        );
    }
} 