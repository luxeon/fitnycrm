package com.fitnycrm.payment.facade;

import com.fitnycrm.payment.facade.mapper.PaymentTariffResponseMapper;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.rest.model.PaymentTariffDetailsResponse;
import com.fitnycrm.payment.rest.model.UpdatePaymentTariffRequest;
import com.fitnycrm.payment.service.PaymentTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentTariffFacade {

    private final PaymentTariffService paymentTariffService;
    private final PaymentTariffResponseMapper responseMapper;

    public PaymentTariffDetailsResponse create(UUID tenantId, UUID trainingId, CreatePaymentTariffRequest request) {
        return responseMapper.toResponse(paymentTariffService.create(tenantId, trainingId, request));
    }

    public PaymentTariffDetailsResponse findById(UUID tenantId, UUID trainingId, UUID tariffId) {
        return responseMapper.toResponse(paymentTariffService.findById(tenantId, trainingId, tariffId));
    }

    public PaymentTariffDetailsResponse update(UUID tenantId, UUID trainingId, UUID tariffId, UpdatePaymentTariffRequest request) {
        return responseMapper.toResponse(paymentTariffService.update(tenantId, trainingId, tariffId, request));
    }

    public void delete(UUID tenantId, UUID trainingId, UUID tariffId) {
        paymentTariffService.delete(tenantId, trainingId, tariffId);
    }
} 