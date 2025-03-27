package com.fitnycrm.training.facade;

import com.fitnycrm.training.facade.mapper.TrainingResponseMapper;
import com.fitnycrm.training.rest.model.CreateTrainingRequest;
import com.fitnycrm.training.rest.model.TrainingDetailsResponse;
import com.fitnycrm.training.rest.model.TrainingPageItemResponse;
import com.fitnycrm.training.rest.model.UpdateTrainingRequest;
import com.fitnycrm.training.service.TrainingService;
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
} 