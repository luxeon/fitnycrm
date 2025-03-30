package com.fitnycrm.user.rest.client.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Client signup request")
public record SignupClientRequest(
        @Schema(description = "Client's first name", example = "John")
        @NotNull
        @Size(min = 2, max = 255)
        String firstName,

        @Schema(description = "Client's last name", example = "Doe")
        @NotNull
        @Size(min = 2, max = 255)
        String lastName,

        @Schema(description = "Client's password", example = "password123")
        @NotNull
        @Size(min = 8, max = 255)
        String password
) {
} 