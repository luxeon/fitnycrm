package com.fitnycrm.training.service;

import com.fitnycrm.training.entity.Training;
import com.fitnycrm.training.repository.TrainingRepository;
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

    @Transactional
    public Training create(Training training) {
        return trainingRepository.save(training);
    }

    @Transactional(readOnly = true)
    public Training findById(UUID tenantId, UUID id) {
        return trainingRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));
    }

    @Transactional(readOnly = true)
    public Page<Training> findByTenantId(UUID tenantId, Pageable pageable) {
        return trainingRepository.findByTenantId(tenantId, pageable);
    }

    @Transactional
    public Training update(UUID tenantId, UUID id, Training training) {
        Training existingTraining = findById(tenantId, id);
        existingTraining.setName(training.getName());
        existingTraining.setDescription(training.getDescription());
        existingTraining.setDurationMinutes(training.getDurationMinutes());
        existingTraining.setClientCapacity(training.getClientCapacity());
        return trainingRepository.save(existingTraining);
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        Training training = findById(tenantId, id);
        trainingRepository.delete(training);
    }
} 