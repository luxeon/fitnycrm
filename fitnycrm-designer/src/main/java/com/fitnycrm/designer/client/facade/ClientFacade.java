package com.fitnycrm.designer.client.facade;

import com.fitnycrm.designer.client.facade.mapper.ClientMapper;
import com.fitnycrm.designer.client.rest.model.ClientDetailsResponse;
import com.fitnycrm.designer.client.rest.model.ClientSignupRequest;
import com.fitnycrm.designer.client.service.ClientService;
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
} 