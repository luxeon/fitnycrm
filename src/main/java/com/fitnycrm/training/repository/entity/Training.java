package com.fitnycrm.training.repository.entity;

import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.tenant.repository.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "trainings")
@ToString(exclude = {"tenant", "paymentTariffs"})
@EqualsAndHashCode(exclude = {"tenant", "paymentTariffs"})
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @Min(1)
    @Column(nullable = false)
    private Integer durationMinutes;

    @Min(1)
    @Column(nullable = false)
    private Integer clientCapacity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Training_Payment_Tariffs",
            joinColumns = {@JoinColumn(name = "training_id")},
            inverseJoinColumns = {@JoinColumn(name = "payment_tariff_id")}
    )
    private Set<PaymentTariff> paymentTariffs = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 