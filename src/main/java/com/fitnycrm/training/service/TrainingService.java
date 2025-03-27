package com.fitnycrm.training.service;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.repository.TrainingRepository;
import com.fitnycrm.training.rest.model.CreateTrainingRequest;
import com.fitnycrm.training.rest.model.UpdateTrainingRequest;
import com.fitnycrm.training.service.exception.TrainingNotFoundException;
import com.fitnycrm.training.service.mapper.TrainingRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingRequestMapper requestMapper;
    private final TenantService tenantService;

    @Transactional
    public Training create(UUID tenantId, CreateTrainingRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        Training training = requestMapper.toTraining(request);
        training.setTenant(tenant);
        return trainingRepository.save(training);
    }

    @Transactional
    public Training update(UUID tenantId, UUID id, UpdateTrainingRequest request) {
        Training training = findById(tenantId, id);
        requestMapper.update(training, request);
        return trainingRepository.save(training);
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        Training training = findById(tenantId, id);
        trainingRepository.delete(training);
    }

    @Transactional(readOnly = true)
    public Training findById(UUID tenantId, UUID id) {
        return trainingRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new TrainingNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Training> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingRepository.findByTenantId(tenantId, pageable);
    }
} 