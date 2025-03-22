package com.fittrackcrm.core.user.service.exception;

import com.fittrackcrm.core.user.repository.entity.UserRole;
import lombok.Getter;

@Getter
public class RoleNotFoundException extends RuntimeException {

    private final UserRole.Name name;

    public RoleNotFoundException(UserRole.Name name) {
        super("Role '" + name + "' not found in the database");
        this.name = name;
    }
} 