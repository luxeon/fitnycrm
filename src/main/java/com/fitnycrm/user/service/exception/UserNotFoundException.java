package com.fitnycrm.user.service.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final UUID id;

    public UserNotFoundException(UUID id) {
        super("User not found with id " + id);
        this.id = id;
    }
}
