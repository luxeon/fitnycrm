package com.fitnycrm.tenant.repository.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.fitnycrm.user.repository.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "Tenant_Users",
            joinColumns = { @JoinColumn(name = "tenantId") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> users;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 