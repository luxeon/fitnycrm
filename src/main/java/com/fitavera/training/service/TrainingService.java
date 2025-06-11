package com.fitavera.training.service;

import com.fitavera.payment.repository.PaymentTariffRepository;
import com.fitavera.payment.repository.entity.PaymentTariff;
import com.fitavera.tenant.repository.entity.Tenant;
import com.fitavera.tenant.service.TenantService;
import com.fitavera.training.repository.TrainingRepository;
import com.fitavera.training.repository.entity.Training;
import com.fitavera.training.rest.model.CreateTrainingRequest;
import com.fitavera.training.rest.model.UpdateTrainingPaymentTariffsRequest;
import com.fitavera.training.rest.model.UpdateTrainingRequest;
import com.fitavera.training.service.exception.TrainingNotFoundException;
import com.fitavera.training.service.mapper.TrainingRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingRequestMapper requestMapper;
    private final TenantService tenantService;
    private final PaymentTariffRepository paymentTariffRepository;

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

    @Transactional
    public void updateTariffs(UUID tenantId, UUID trainingId, UpdateTrainingPaymentTariffsRequest request) {
        Training training = findById(tenantId, trainingId);

        Set<PaymentTariff> tariffs = new HashSet<>(paymentTariffRepository.findAllById(request.tariffIds()));

        // Replace tariffs
        training.getPaymentTariffs().clear();
        training.getPaymentTariffs().addAll(tariffs);

        trainingRepository.save(training);
    }

    @Transactional(readOnly = true)
    public Set<PaymentTariff> getTariffs(UUID tenantId, UUID trainingId) {
        Training training = findById(tenantId, trainingId);
        return training.getPaymentTariffs();
    }
} 