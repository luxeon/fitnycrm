package com.fitnycrm.user.service.exception;

import com.fitnycrm.user.repository.entity.UserRole;
import lombok.Getter;

@Getter
public class RoleNotFoundException extends RuntimeException {

    private final UserRole.Name name;

    public RoleNotFoundException(UserRole.Name name) {
        super("Role '" + name + "' not found in the database");
        this.name = name;
    }
} 