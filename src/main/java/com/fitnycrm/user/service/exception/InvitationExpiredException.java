package com.fitnycrm.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException(UUID id) {
        super("Invitation with id " + id + " has expired");
    }
} 