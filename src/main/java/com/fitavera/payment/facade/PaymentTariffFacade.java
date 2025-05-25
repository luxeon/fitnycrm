package com.fitavera.payment.facade;

import com.fitavera.payment.facade.mapper.PaymentTariffResponseMapper;
import com.fitavera.payment.rest.model.CreatePaymentTariffRequest;
import com.fitavera.payment.rest.model.PaymentTariffDetailsResponse;
import com.fitavera.payment.rest.model.PaymentTariffListItemResponse;
import com.fitavera.payment.rest.model.UpdatePaymentTariffRequest;
import com.fitavera.payment.service.PaymentTariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class PaymentTariffFacade {

    private final PaymentTariffService paymentTariffService;
    private final PaymentTariffResponseMapper responseMapper;

    public List<PaymentTariffListItemResponse> findAll(UUID tenantId) {
        return paymentTariffService.findAll(tenantId).stream()
                .map(responseMapper::toListItemResponse)
                .toList();
    }

    public PaymentTariffDetailsResponse create(UUID tenantId, CreatePaymentTariffRequest request) {
        return responseMapper.toResponse(paymentTariffService.create(tenantId, request));
    }

    public PaymentTariffDetailsResponse findById(UUID tenantId, UUID tariffId) {
        return responseMapper.toResponse(paymentTariffService.findById(tenantId, tariffId));
    }

    public PaymentTariffDetailsResponse update(UUID tenantId, UUID tariffId, UpdatePaymentTariffRequest request) {
        return responseMapper.toResponse(paymentTariffService.update(tenantId, tariffId, request));
    }

    public void delete(UUID tenantId, UUID tariffId) {
        paymentTariffService.delete(tenantId, tariffId);
    }

    public Set<PaymentTariffListItemResponse> findAllByTrainingId(UUID tenantId, UUID trainingId) {
        return paymentTariffService.findAllByTrainingId(tenantId, trainingId).stream()
                .map(responseMapper::toListItemResponse)
                .collect(toSet());
    }
} 