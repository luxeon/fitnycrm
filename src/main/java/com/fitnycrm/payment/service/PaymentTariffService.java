package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.PaymentTariffRepository;
import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.service.mapper.PaymentTariffRequestMapper;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTariffService {

    private final PaymentTariffRepository paymentTariffRepository;
    private final PaymentTariffRequestMapper requestMapper;
    private final TrainingService trainingService;

    @Transactional
    public PaymentTariff create(UUID tenantId, UUID trainingId, CreatePaymentTariffRequest request) {
        Training training = trainingService.findById(tenantId, trainingId);
        PaymentTariff paymentTariff = requestMapper.toEntity(request);
        paymentTariff.setTraining(training);
        return paymentTariffRepository.save(paymentTariff);
    }
} 