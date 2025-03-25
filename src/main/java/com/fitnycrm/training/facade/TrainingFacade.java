package com.fitnycrm.training.facade;

import com.fitnycrm.training.entity.Training;
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

    public TrainingDetailsResponse create(UUID tenantId, CreateTrainingRequest request) {
        Training training = trainingService.create(
                tenantId,
                request.name(),
                request.description(),
                request.durationMinutes(),
                request.clientCapacity()
        );
        return toResponse(training);
    }

    public TrainingDetailsResponse findById(UUID tenantId, UUID id) {
        return toResponse(trainingService.findById(tenantId, id));
    }

    public Page<TrainingPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingService.findByTenantId(tenantId, pageable)
                .map(this::toPageItemResponse);
    }

    public TrainingDetailsResponse update(UUID tenantId, UUID id, UpdateTrainingRequest request) {
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

    private TrainingDetailsResponse toResponse(Training training) {
        return new TrainingDetailsResponse(
                training.getId(),
                training.getName(),
                training.getDescription(),
                training.getDurationMinutes(),
                training.getClientCapacity(),
                training.getCreatedAt(),
                training.getUpdatedAt()
        );
    }

    private TrainingPageItemResponse toPageItemResponse(Training training) {
        return new TrainingPageItemResponse(
                training.getId(),
                training.getName(),
                training.getDescription(),
                training.getDurationMinutes(),
                training.getClientCapacity(),
                training.getCreatedAt(),
                training.getUpdatedAt()
        );
    }
} 