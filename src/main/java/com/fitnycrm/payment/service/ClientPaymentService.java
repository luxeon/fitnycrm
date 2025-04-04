package com.fitnycrm.payment.service;

import com.fitnycrm.payment.repository.ClientPaymentRepository;
import com.fitnycrm.payment.repository.ClientTrainingCreditRepository;
import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.repository.entity.ClientTrainingCredit;
import com.fitnycrm.payment.repository.entity.ClientTrainingCreditTrigger;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import com.fitnycrm.payment.repository.specification.ClientPaymentSpecification;
import com.fitnycrm.payment.rest.model.ClientPaymentFilterRequest;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
import com.fitnycrm.payment.rest.model.ExtendedClientPaymentFilterRequest;
import com.fitnycrm.payment.service.exception.ClientPaymentAlreadyCancelledException;
import com.fitnycrm.payment.service.exception.ClientPaymentCannotBeCancelled;
import com.fitnycrm.payment.service.exception.ClientPaymentNotFoundException;
import com.fitnycrm.payment.service.exception.ClientTrainingCreditNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientPaymentService {

    private final ClientPaymentRepository clientPaymentRepository;
    private final ClientTrainingCreditRepository clientTrainingCreditRepository;
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

        ClientTrainingCredit credit = createCredit(request, client, training);
        payment.setCredit(credit);

        return clientPaymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public ClientTrainingCredit getCreditsSummary(UUID tenantId, UUID clientId, UUID trainingId) {
        User client = clientService.findById(tenantId, clientId);
        Training training = trainingService.findById(tenantId, trainingId);

        return clientTrainingCreditRepository.findFirstByClientAndTrainingOrderByCreatedAtDesc(client, training)
                .orElseThrow(() -> new ClientTrainingCreditNotFoundException(clientId, trainingId));
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
        if (PaymentStatus.CANCELED.equals(payment.getStatus())) {
            throw new ClientPaymentAlreadyCancelledException(paymentId);
        }
        payment.setStatus(PaymentStatus.CANCELED);
        ClientTrainingCredit credit = payment.getCredit();
        clientTrainingCreditRepository.findFirstByClientAndTrainingOrderByCreatedAtDesc(payment.getClient(), payment.getTraining()).ifPresent(lastCredit -> {
            if (!lastCredit.equals(credit)) {
                throw new ClientPaymentCannotBeCancelled("Payment can be cancelled only when it's connected to the latest training credit record.");
            }
        });
        payment.setCredit(null);
        payment = clientPaymentRepository.save(payment);
        clientTrainingCreditRepository.delete(credit);
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

    private ClientTrainingCredit createCredit(CreateClientPaymentRequest request, User client, Training training) {
        Optional<ClientTrainingCredit> optionalLastCredit = clientTrainingCreditRepository.findFirstByClientAndTrainingOrderByCreatedAtDesc(client, training);
        if (optionalLastCredit.isPresent()) {
            ClientTrainingCredit lastCredit = optionalLastCredit.get();
            if (isExpired(lastCredit)) {
                return addCredit(client, training, request.trainingsCount(), OffsetDateTime.now().plusDays(request.validDays()));
            } else {
                return addCredit(client, training, lastCredit.getRemainingTrainings() + request.trainingsCount(), lastCredit.getExpiresAt().plusDays(request.validDays()));
            }
        } else {
            return addCredit(client, training, request.trainingsCount(), OffsetDateTime.now().plusDays(request.validDays()));
        }
    }

    private ClientTrainingCredit addCredit(User client, Training training, int remainingTrainings, OffsetDateTime expiresAt) {
        ClientTrainingCredit subscription = new ClientTrainingCredit();
        subscription.setClient(client);
        subscription.setTraining(training);
        subscription.setRemainingTrainings(remainingTrainings);
        subscription.setExpiresAt(expiresAt);
        subscription.setTrigger(ClientTrainingCreditTrigger.PAYMENT);
        return clientTrainingCreditRepository.save(subscription);
    }

    private static boolean isExpired(ClientTrainingCredit credit) {
        return credit.getExpiresAt().isBefore(OffsetDateTime.now());
    }
} 