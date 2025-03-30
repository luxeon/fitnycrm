package com.fitnycrm.user.repository.entity;

import com.fitnycrm.tenant.repository.entity.Tenant;
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

    @Column(name = "inviter_name", nullable = false)
    private String inviterName;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
} 