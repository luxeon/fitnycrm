package com.fitnycrm.tenant.repository.entity;

import java.time.OffsetDateTime;
import java.util.*;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "tenants")
@ToString(exclude = {"users", "locations", "trainings"})
@EqualsAndHashCode(exclude = {"users", "locations", "trainings"})
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "Tenant_Users",
            joinColumns = {@JoinColumn(name = "tenantId")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    private Set<Training> trainings = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 