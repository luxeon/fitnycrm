package com.fitavera.payment.repository.entity;

import com.fitavera.training.repository.entity.Training;
import com.fitavera.user.repository.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "client_training_credits")
@ToString(exclude = {"client", "training"})
@EqualsAndHashCode(exclude = {"client", "training"})
public class ClientTrainingCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Column(nullable = false)
    private Integer remainingTrainings;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientTrainingCreditTrigger trigger;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
