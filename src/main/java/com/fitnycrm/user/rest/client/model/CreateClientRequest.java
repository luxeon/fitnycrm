package com.fitnycrm.user.rest.client.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "User signup request")
public record CreateClientRequest(

    @Schema(description = "User's first name", example = "John")
    @NotNull
    @Size(min = 2, max = 255)
    String firstName,

    @Schema(description = "User's last name", example = "Doe")
    @NotNull
    @Size(min = 2, max = 255)
    String lastName,

    @Schema(description = "User's email", example = "john.doe@example.com")
    @NotNull
    @Email
    String email,

    @Schema(description = "User's phone number in E.164 format", example = "+1234567890")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
    String phoneNumber
) {}