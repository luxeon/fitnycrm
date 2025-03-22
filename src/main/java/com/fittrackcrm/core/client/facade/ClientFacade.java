package com.fittrackcrm.core.client.facade;

import com.fittrackcrm.core.client.facade.mapper.ClientMapper;
import com.fittrackcrm.core.client.rest.model.ClientDetailsResponse;
import com.fittrackcrm.core.client.rest.model.ClientSignupRequest;
import com.fittrackcrm.core.client.service.ClientService;
import com.fittrackcrm.core.user.service.UserService;
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