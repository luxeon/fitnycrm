package com.fitnycrm.user.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException(String token) {
        super("Invitation with token " + token + " has expired");
    }
} 