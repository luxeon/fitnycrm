package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.PaymentTariffRepository;
import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.rest.model.UpdatePaymentTariffRequest;
import com.fitnycrm.payment.service.exception.PaymentTariffNotFoundException;
import com.fitnycrm.payment.service.mapper.PaymentTariffRequestMapper;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTariffService {

    private final PaymentTariffRepository paymentTariffRepository;
    private final PaymentTariffRequestMapper requestMapper;
    private final TenantService tenantService;

    @Transactional(readOnly = true)
    public List<PaymentTariff> findAll(UUID tenantId) {
        Tenant tenant = tenantService.findById(tenantId);
        return paymentTariffRepository.findAllByTenant(tenant);
    }

    @Transactional
    public PaymentTariff create(UUID tenantId, CreatePaymentTariffRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        PaymentTariff paymentTariff = requestMapper.toEntity(request);
        paymentTariff.setTenant(tenant);
        return paymentTariffRepository.save(paymentTariff);
    }

    @Transactional
    public PaymentTariff update(UUID tenantId, UUID tariffId, UpdatePaymentTariffRequest request) {
        PaymentTariff paymentTariff = findById(tenantId, tariffId);
        requestMapper.update(paymentTariff, request);
        return paymentTariffRepository.save(paymentTariff);
    }

    @Transactional
    public void delete(UUID tenantId, UUID tariffId) {
        PaymentTariff paymentTariff = findById(tenantId, tariffId);
        paymentTariffRepository.delete(paymentTariff);
    }

    @Transactional(readOnly = true)
    public PaymentTariff findById(UUID tenantId, UUID tariffId) {
        PaymentTariff paymentTariff = paymentTariffRepository.findById(tariffId)
                .orElseThrow(() -> new PaymentTariffNotFoundException(tariffId));

        if (!paymentTariff.getTenant().getId().equals(tenantId)) {
            throw new PaymentTariffNotFoundException(tariffId);
        }
        return paymentTariff;
    }
} 