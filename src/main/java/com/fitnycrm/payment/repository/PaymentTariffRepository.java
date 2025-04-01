package com.fitnycrm.payment.repository;

import com.fitnycrm.payment.repository.entity.PaymentTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentTariffRepository extends JpaRepository<PaymentTariff, UUID> {

} 