package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.PaymentTariffRepository;
import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.rest.model.UpdatePaymentTariffRequest;
import com.fitnycrm.payment.service.exception.PaymentTariffNotFoundException;
import com.fitnycrm.payment.service.mapper.PaymentTariffRequestMapper;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTariffService {

    private final PaymentTariffRepository paymentTariffRepository;
    private final PaymentTariffRequestMapper requestMapper;
    private final TrainingService trainingService;

    @Transactional(readOnly = true)
    public List<PaymentTariff> findAll(UUID tenantId, UUID trainingId) {
        Training training = trainingService.findById(tenantId, trainingId);
        return paymentTariffRepository.findAllByTraining(training);
    }

    @Transactional
    public PaymentTariff create(UUID tenantId, UUID trainingId, CreatePaymentTariffRequest request) {
        Training training = trainingService.findById(tenantId, trainingId);
        PaymentTariff paymentTariff = requestMapper.toEntity(request);
        paymentTariff.setTraining(training);
        return paymentTariffRepository.save(paymentTariff);
    }

    @Transactional
    public PaymentTariff update(UUID tenantId, UUID trainingId, UUID tariffId, UpdatePaymentTariffRequest request) {
        PaymentTariff paymentTariff = findById(tenantId, trainingId, tariffId);
        requestMapper.update(paymentTariff, request);
        return paymentTariffRepository.save(paymentTariff);
    }

    @Transactional
    public void delete(UUID tenantId, UUID trainingId, UUID tariffId) {
        PaymentTariff paymentTariff = findById(tenantId, trainingId, tariffId);
        paymentTariffRepository.delete(paymentTariff);
    }

    @Transactional(readOnly = true)
    public PaymentTariff findById(UUID tenantId, UUID trainingId, UUID tariffId) {
        PaymentTariff paymentTariff = paymentTariffRepository.findById(tariffId)
                .orElseThrow(() -> new PaymentTariffNotFoundException(tariffId));

        if (!paymentTariff.getTraining().getId().equals(trainingId)) {
            throw new PaymentTariffNotFoundException(tariffId);
        }

        if (!paymentTariff.getTraining().getTenant().getId().equals(tenantId)) {
            throw new PaymentTariffNotFoundException(tariffId);
        }
        return paymentTariff;
    }
} 