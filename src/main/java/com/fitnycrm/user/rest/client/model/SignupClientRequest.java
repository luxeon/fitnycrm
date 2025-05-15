package com.fitnycrm.user.rest.client.model;

import com.fitnycrm.common.validation.locale.SupportedLocale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

        @Schema(description = "User's phone number in E.164 format", example = "+1234567890")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
        String phoneNumber,

        @Schema(description = "Client's password", example = "password123")
        @NotNull
        @Size(min = 8, max = 255)
        String password,

        @Schema(description = "Client's locale", example = "en")
        @NotNull
        @SupportedLocale
        String locale
) {
} 