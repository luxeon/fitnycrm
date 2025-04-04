package com.fitnycrm.payment.facade;

import com.fitnycrm.payment.facade.mapper.ClientPaymentMapper;
import com.fitnycrm.payment.rest.model.ClientPaymentDetailsResponse;
import com.fitnycrm.payment.rest.model.ClientPaymentFilterRequest;
import com.fitnycrm.payment.rest.model.ClientPaymentPageItemResponse;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
import com.fitnycrm.payment.service.ClientPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientPaymentFacade {

    private final ClientPaymentService clientPaymentService;
    private final ClientPaymentMapper clientPaymentMapper;

    public ClientPaymentDetailsResponse create(UUID tenantId, UUID clientId, CreateClientPaymentRequest request) {
        return clientPaymentMapper.toDetailsResponse(
                clientPaymentService.create(tenantId, clientId, request)
        );
    }

    public ClientPaymentDetailsResponse cancel(UUID tenantId, UUID clientId, UUID paymentId) {
        return clientPaymentMapper.toDetailsResponse(
                clientPaymentService.cancel(tenantId, clientId, paymentId)
        );
    }

    public Page<ClientPaymentPageItemResponse> findAll(UUID tenantId, UUID clientId, ClientPaymentFilterRequest filter, Pageable pageable) {
        return clientPaymentService.findAll(tenantId, clientId, filter, pageable)
                .map(clientPaymentMapper::toPageItemResponse);
    }
} 