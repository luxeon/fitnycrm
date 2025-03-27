package com.fitnycrm.user.rest.trainer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new trainer")
public record CreateTrainerRequest(
        @Schema(description = "First name", example = "John")
        @NotNull
        @Size(min = 1, max = 255)
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        @NotNull
        @Size(min = 1, max = 255)
        String lastName,

        @Schema(description = "Email address", example = "john.doe@example.com")
        @NotNull
        @Email
        String email,

        @Schema(description = "Phone number", example = "+1234567890")
        @Size(max = 20)
        String phoneNumber
) {
} 