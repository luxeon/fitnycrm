package com.fitavera.payment.repository;

import com.fitavera.payment.repository.entity.ClientTrainingCredit;
import com.fitavera.training.repository.entity.Training;
import com.fitavera.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientTrainingCreditRepository extends JpaRepository<ClientTrainingCredit, UUID> {
    Optional<ClientTrainingCredit> findFirstByClientAndTrainingOrderByCreatedAtDesc(User client, Training training);
}
