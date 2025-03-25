package com.fitnycrm.client.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Client update request")
public record ClientUpdateRequest(
    @Schema(description = "Client's first name", example = "John")
    @NotNull
    @Size(min = 2, max = 255)
    String firstName,

    @Schema(description = "Client's last name", example = "Doe")
    @NotNull
    @Size(min = 2, max = 255)
    String lastName,

    @Schema(description = "Client's email", example = "john.doe@example.com")
    @NotNull
    @Email
    String email,

    @Schema(description = "Client's phone number in E.164 format", example = "+1234567890")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    String phoneNumber
) {} 