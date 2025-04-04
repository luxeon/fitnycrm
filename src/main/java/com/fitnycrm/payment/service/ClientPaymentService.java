package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.ClientPaymentRepository;
import com.fitnycrm.payment.repository.ClientTrainingSubscriptionRepository;
import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.repository.entity.ClientTrainingSubscription;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import com.fitnycrm.payment.repository.specification.ClientPaymentSpecification;
import com.fitnycrm.payment.rest.model.ClientPaymentFilterRequest;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
import com.fitnycrm.payment.rest.model.ExtendedClientPaymentFilterRequest;
import com.fitnycrm.payment.service.exception.ClientPaymentNotFoundException;
import com.fitnycrm.payment.service.mapper.ClientPaymentRequestMapper;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.tenant.service.TenantService;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.service.TrainingService;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientPaymentService {

    private final ClientPaymentRepository clientPaymentRepository;
    private final ClientTrainingSubscriptionRepository trainingSubscriptionRepository;
    private final TenantService tenantService;
    private final ClientService clientService;
    private final TrainingService trainingService;
    private final ClientPaymentRequestMapper mapper;

    @Transactional
    public ClientPayment create(UUID tenantId, UUID clientId, CreateClientPaymentRequest request) {
        Tenant tenant = tenantService.findById(tenantId);
        User client = clientService.findById(tenantId, clientId);
        Training training = trainingService.findById(tenantId, request.trainingId());

        ClientPayment payment = mapper.toEntity(request);
        payment.setTenant(tenant);
        payment.setClient(client);
        payment.setTraining(training);

        payment = clientPaymentRepository.save(payment);

        trainingSubscriptionRepository.findFirstByClientAndTrainingOrderByCreatedAtDesc(client, training).ifPresentOrElse(subscription -> {
            if (isExpired(subscription)) {
                createSubscription(client, training, request.trainingsCount(), OffsetDateTime.now().plusDays(request.validDays()));
            } else {
                createSubscription(client, training, subscription.getRemainingTrainings() + request.trainingsCount(), subscription.getExpiresAt().plusDays(request.validDays()));
            }
        }, () -> createSubscription(client, training, request.trainingsCount(), OffsetDateTime.now().plusDays(request.validDays())));

        return payment;
    }

    private void createSubscription(User client, Training training, int remainingTrainings, OffsetDateTime expiresAt) {
        ClientTrainingSubscription subscription = new ClientTrainingSubscription();
        subscription.setClient(client);
        subscription.setTraining(training);
        subscription.setRemainingTrainings(remainingTrainings);
        subscription.setExpiresAt(expiresAt);
        trainingSubscriptionRepository.save(subscription);
    }

    private static boolean isExpired(ClientTrainingSubscription subscription) {
        return subscription.getExpiresAt().isBefore(OffsetDateTime.now());
    }

    @Transactional(readOnly = true)
    public ClientPayment findById(UUID tenantId, UUID clientId, UUID paymentId) {
        ClientPayment payment = clientPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new ClientPaymentNotFoundException(paymentId));

        if (!payment.getTenant().getId().equals(tenantId) || !payment.getClient().getId().equals(clientId)) {
            throw new ClientPaymentNotFoundException(paymentId);
        }

        return payment;
    }

    @Transactional
    public ClientPayment cancel(UUID tenantId, UUID clientId, UUID paymentId) {
        ClientPayment payment = findById(tenantId, clientId, paymentId);
        payment.setStatus(PaymentStatus.CANCELED);
        payment = clientPaymentRepository.save(payment);

        trainingSubscriptionRepository.findFirstByClientAndTrainingOrderByCreatedAtDesc(payment.getClient(), payment.getTraining())
                .ifPresent(trainingSubscriptionRepository::delete);

        return payment;
    }

    @Transactional(readOnly = true)
    public Page<ClientPayment> findAll(UUID tenantId, UUID clientId, ClientPaymentFilterRequest filter, Pageable pageable) {
        return clientPaymentRepository.findAll(
                ClientPaymentSpecification.withFilters(tenantId, clientId, filter),
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<ClientPayment> findAllInTenant(UUID tenantId, ExtendedClientPaymentFilterRequest filter, Pageable pageable) {
        return clientPaymentRepository.findAll(
                ClientPaymentSpecification.withTenantFilters(tenantId, filter),
                pageable
        );
    }
} 