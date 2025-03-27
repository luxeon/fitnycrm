package com.fitnycrm.tenant.service.exception;

public class TenantAlreadyCreatedException extends RuntimeException{

    public TenantAlreadyCreatedException() {
        super("User already has a tenant assigned");
    }

}
