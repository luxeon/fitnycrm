package com.fittrackcrm.core.user.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Name name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public enum Name {

        ADMIN, INSTRUCTOR, CLIENT

    }
}
