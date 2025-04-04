package com.fitnycrm.payment.repository;

import com.fitnycrm.payment.repository.entity.ClientTrainingSubscription;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientTrainingSubscriptionRepository extends JpaRepository<ClientTrainingSubscription, UUID> {
    Optional<ClientTrainingSubscription> findFirstByClientAndTrainingOrderByCreatedAtDesc(User client, Training training);
}
