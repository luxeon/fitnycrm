package com.fitnycrm.payment.repository;

import com.fitnycrm.payment.repository.entity.ClientPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientPaymentRepository extends JpaRepository<ClientPayment, UUID>, JpaSpecificationExecutor<ClientPayment> {
} 