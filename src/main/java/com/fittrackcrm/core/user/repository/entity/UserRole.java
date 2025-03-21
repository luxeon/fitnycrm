package com.fittrackcrm.core.user.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Name name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> employees = new HashSet<>();

    public enum Name {

        ADMIN

    }
}
