package com.fitavera.payment.repository;

import com.fitavera.payment.repository.entity.ClientPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientPaymentRepository extends JpaRepository<ClientPayment, UUID>, JpaSpecificationExecutor<ClientPayment> {
} 