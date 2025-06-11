package com.fitavera.user.rest.trainer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTrainerRequest(
        @Schema(description = "First name of the trainer", example = "John")
        @NotBlank
        @Size(min = 2, max = 50)
        String firstName,

        @Schema(description = "Last name of the trainer", example = "Doe")
        @NotBlank
        @Size(min = 2, max = 50)
        String lastName,

        @Schema(description = "Email address of the trainer", example = "john.doe@example.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Phone number of the trainer", example = "+1234567890")
        @NotBlank
        @Size(min = 10, max = 15)
        String phoneNumber
) {
}