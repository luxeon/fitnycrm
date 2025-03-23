package com.fitnycrm.designer.security.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
    @NotNull
    @Email
    String email,

    @NotBlank
    String password
) {} 