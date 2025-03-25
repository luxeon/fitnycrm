package com.fitnycrm.training.facade;

import com.fitnycrm.training.entity.Training;
import com.fitnycrm.training.facade.mapper.TrainingMapper;
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
    private final TrainingMapper mapper;

    public TrainingDetailsResponse create(UUID tenantId, CreateTrainingRequest request) {
        return mapper.toDetailsResponse(
                trainingService.create(
                        mapper.toEntity(tenantId, request)
                )
        );
    }

    public TrainingDetailsResponse findById(UUID tenantId, UUID id) {
        return mapper.toDetailsResponse(
                trainingService.findById(tenantId, id)
        );
    }

    public Page<TrainingPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingService.findByTenantId(tenantId, pageable)
                .map(mapper::toPageItemResponse);
    }

    public TrainingDetailsResponse update(UUID tenantId, UUID id, UpdateTrainingRequest request) {
        return mapper.toDetailsResponse(
                trainingService.update(
                        tenantId,
                        id,
                        mapper.toEntity(tenantId, request)
                )
        );
    }

    public void delete(UUID tenantId, UUID id) {
        trainingService.delete(tenantId, id);
    }
} 