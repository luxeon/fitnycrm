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
    public Training create(UUID tenantId, String name, String description, Integer durationMinutes, Integer clientCapacity) {

        Training training = new Training();
        training.setTenantId(tenantId);
        training.setName(name);
        training.setDescription(description);
        training.setDurationMinutes(durationMinutes);
        training.setClientCapacity(clientCapacity);

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
    public Training update(UUID tenantId, UUID id, String name, String description, Integer durationMinutes, Integer clientCapacity) {
        Training training = findById(tenantId, id);
        training.setName(name);
        training.setDescription(description);
        training.setDurationMinutes(durationMinutes);
        training.setClientCapacity(clientCapacity);
        return trainingRepository.save(training);
    }

    @Transactional
    public void delete(UUID tenantId, UUID id) {
        Training training = findById(tenantId, id);
        trainingRepository.delete(training);
    }
} 