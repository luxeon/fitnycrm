package com.fitnycrm.payment.repository;

import com.fitnycrm.payment.repository.entity.ClientTrainingCredit;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientTrainingCreditRepository extends JpaRepository<ClientTrainingCredit, UUID> {
    Optional<ClientTrainingCredit> findFirstByClientAndTrainingOrderByCreatedAtDesc(User client, Training training);
}
