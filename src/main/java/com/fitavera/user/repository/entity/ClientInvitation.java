package com.fitavera.user.repository.entity;

import com.fitavera.tenant.repository.entity.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "client_invitations")
@ToString(exclude = "tenant")
@EqualsAndHashCode(exclude = "tenant")
public class ClientInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User inviter;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
} 