package com.fitnycrm.training.facade;

import com.fitnycrm.training.entity.Training;
import com.fitnycrm.training.rest.model.TrainingRequest;
import com.fitnycrm.training.rest.model.TrainingResponse;
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

    public TrainingResponse create(UUID tenantId, TrainingRequest request) {
        Training training = trainingService.create(
                tenantId,
                request.name(),
                request.description(),
                request.durationMinutes(),
                request.clientCapacity()
        );
        return toResponse(training);
    }

    public TrainingResponse findById(UUID tenantId, UUID id) {
        return toResponse(trainingService.findById(tenantId, id));
    }

    public Page<TrainingResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingService.findByTenantId(tenantId, pageable)
                .map(this::toResponse);
    }

    public TrainingResponse update(UUID tenantId, UUID id, TrainingRequest request) {
        Training training = trainingService.update(
                tenantId,
                id,
                request.name(),
                request.description(),
                request.durationMinutes(),
                request.clientCapacity()
        );
        return toResponse(training);
    }

    public void delete(UUID tenantId, UUID id) {
        trainingService.delete(tenantId, id);
    }

    private TrainingResponse toResponse(Training training) {
        return new TrainingResponse(
                training.getId(),
                training.getName(),
                training.getDescription(),
                training.getDurationMinutes(),
                training.getClientCapacity()
        );
    }
} 