package com.fitavera.training.facade;

import com.fitavera.training.facade.mapper.TrainingResponseMapper;
import com.fitavera.training.rest.model.*;
import com.fitavera.training.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

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