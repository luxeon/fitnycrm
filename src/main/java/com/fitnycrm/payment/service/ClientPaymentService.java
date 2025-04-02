package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.ClientPaymentRepository;
import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
import com.fitnycrm.payment.service.exception.PaymentNotFoundException;
import com.fitnycrm.payment.service.mapper.ClientPaymentRequestMapper;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientPaymentService {

    private final ClientPaymentRepository clientPaymentRepository;
    private final TenantService tenantService;
    private final ClientService clientService;
    private final ClientPaymentRequestMapper mapper;

    @Transactional
    public ClientPayment create(UUID tenantId, UUID clientId, CreateClientPaymentRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        User client = clientService.findById(tenantId, clientId);

        ClientPayment payment = mapper.toEntity(request);
        payment.setTenant(tenant);
        payment.setClient(client);

        return clientPaymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public ClientPayment findById(UUID tenantId, UUID clientId, UUID paymentId) {
        ClientPayment payment = clientPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (!payment.getTenant().getId().equals(tenantId) || !payment.getClient().getId().equals(clientId)) {
            throw new PaymentNotFoundException(paymentId);
        }

        return payment;
    }

    @Transactional
    public ClientPayment cancel(UUID tenantId, UUID clientId, UUID paymentId) {
        ClientPayment payment = findById(tenantId, clientId, paymentId);
        payment.setStatus(PaymentStatus.CANCELED);
        return clientPaymentRepository.save(payment);
    }
} 