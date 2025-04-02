package com.fitnycrm.payment.repository.entity;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.payment.model.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "payment_tariffs")
@ToString(exclude = {"tenant", "trainings"})
@EqualsAndHashCode(exclude = {"tenant", "trainings"})
public class PaymentTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToMany(mappedBy = "paymentTariffs")
    @JoinColumn(name = "training_id", nullable = false)
    private Set<Training> trainings;

    @Min(1)
    @Column(nullable = false)
    private Integer trainingsCount;

    @Min(1)
    @Column(nullable = false)
    private Integer validDays;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 