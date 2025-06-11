package com.fitavera.tenant.service.exception;

public class TenantAlreadyCreatedException extends RuntimeException {

    public TenantAlreadyCreatedException() {
        super("User already has a tenant assigned");
    }

}
