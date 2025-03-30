package com.fitnycrm.user.rest.client.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record InviteClientRequest(
    @NotNull
    @Email
    String email
) {} 