package com.fitnycrm.user.facade.trainer;

import com.fitnycrm.user.facade.trainer.mapper.TrainerResponseMapper;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.rest.trainer.model.TrainerDetailsResponse;
import com.fitnycrm.user.rest.trainer.model.TrainerPageItemResponse;
import com.fitnycrm.user.rest.trainer.model.UpdateTrainerRequest;
import com.fitnycrm.user.service.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrainerFacade {
    private final TrainerService trainerService;
    private final TrainerResponseMapper responseMapper;

    public TrainerDetailsResponse create(UUID tenantId, CreateTrainerRequest request) {
        return responseMapper.toDetailsResponse(
                trainerService.create(tenantId, request)
        );
    }

    public TrainerDetailsResponse update(UUID tenantId, UUID trainerId, UpdateTrainerRequest request) {
        return responseMapper.toDetailsResponse(
                trainerService.update(tenantId, trainerId, request)
        );
    }

    public void delete(UUID tenantId, UUID trainerId) {
        trainerService.delete(tenantId, trainerId);
    }

    public TrainerDetailsResponse findById(UUID tenantId, UUID trainerId) {
        return responseMapper.toDetailsResponse(
                trainerService.findById(tenantId, trainerId)
        );
    }

    public Page<TrainerPageItemResponse> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainerService.findByTenantId(tenantId, pageable)
                .map(responseMapper::toPageItemResponse);
    }
} 