package com.fitnycrm.payment.repository.entity;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.user.repository.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Entity
@Table(name = "client_payments")
@ToString(exclude = {"tenant", "client", "training"})
@EqualsAndHashCode(exclude = {"tenant", "client", "training"})
public class ClientPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @OneToOne(fetch = FetchType.LAZY)
    private ClientTrainingCredit credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "trainings_count", nullable = false)
    private Integer trainingsCount;

    @Column(name = "valid_days", nullable = false)
    private Integer validDays;

    @Column(name = "price", nullable = false, precision = 5, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
} 