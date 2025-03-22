package com.fittrackcrm.core.user.service.exception;

public class AdminRoleNotFoundException extends RuntimeException {
    public AdminRoleNotFoundException() {
        super("Admin role not found in the database");
    }
} 