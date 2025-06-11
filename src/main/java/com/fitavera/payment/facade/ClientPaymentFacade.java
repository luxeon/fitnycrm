package com.fitavera.payment.facade;

import com.fitavera.payment.facade.mapper.ClientPaymentMapper;
import com.fitavera.payment.facade.mapper.ClientTrainingCreditMapper;
import com.fitavera.payment.rest.model.*;
import com.fitavera.payment.service.ClientPaymentService;
import com.fitavera.payment.service.exception.ClientTrainingCreditNotFoundException;
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
    private final ClientTrainingCreditMapper creditsResponseMapper;

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

    public Page<ClientPaymentPageItemResponse> findAllInTenant(UUID tenantId, ExtendedClientPaymentFilterRequest filter, Pageable pageable) {
        return clientPaymentService.findAllInTenant(tenantId, filter, pageable)
                .map(clientPaymentMapper::toPageItemResponse);
    }

    public ClientTrainingCreditsSummaryResponse getCreditsSummary(UUID tenantId, UUID clientId, UUID trainingId) {
        try {
            return creditsResponseMapper.toSummaryResponse(
                    clientPaymentService.getCreditsSummary(tenantId, clientId, trainingId)
            );
        } catch (ClientTrainingCreditNotFoundException e) {
            return new ClientTrainingCreditsSummaryResponse(0, null);
        }
    }
} 