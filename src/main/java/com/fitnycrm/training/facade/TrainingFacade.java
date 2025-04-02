package com.fitnycrm.training.facade;

import com.fitnycrm.payment.facade.mapper.PaymentTariffResponseMapper;
import com.fitnycrm.payment.rest.model.PaymentTariffDetailsResponse;
import com.fitnycrm.payment.rest.model.PaymentTariffListItemResponse;
import com.fitnycrm.training.facade.mapper.TrainingResponseMapper;
import com.fitnycrm.training.rest.model.*;
import com.fitnycrm.training.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class TrainingFacade {
    private final TrainingService trainingService;
    private final TrainingResponseMapper responseMapper;

    public TrainingDetailsResponse create(UUID tenantId, CreateTrainingRequest request) {
        return responseMapper.toDetailsResponse(
                trainingService.create(tenantId, request)
        );
    }

    public TrainingDetailsResponse update(UUID tenantId, UUID id, UpdateTrainingRequest request) {
        return responseMapper.toDetailsResponse(
                trainingService.update(tenantId, id, request)
        );
    }

    public void delete(UUID tenantId, UUID id) {
        trainingService.delete(tenantId, id);
    }

    public TrainingDetailsResponse findById(UUID tenantId, UUID id) {
        return responseMapper.toDetailsResponse(
                trainingService.findById(tenantId, id)
        );
    }

    public Page<TrainingPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingService.findByTenantId(tenantId, pageable)
                .map(responseMapper::toPageItemResponse);
    }

    public void updateTariffs(UUID tenantId, UUID trainingId, UpdateTrainingPaymentTariffsRequest request) {
        trainingService.updateTariffs(tenantId, trainingId, request);
    }
} 