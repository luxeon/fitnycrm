package com.fitnycrm.user.rest.model;

import com.fitnycrm.common.validation.locale.SupportedLocale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Admin signup request")
public record CreateAdminRequest(

        @Schema(description = "Admin's first name", example = "John")
        @NotNull
        @Size(min = 2, max = 255)
        String firstName,

        @Schema(description = "Admin's last name", example = "Doe")
        @NotNull
        @Size(min = 2, max = 255)
        String lastName,

        @Schema(description = "Admin's email", example = "john.doe@example.com")
        @NotNull
        @Email
        String email,

        @Schema(description = "Admin's phone number in E.164 format", example = "+1234567890")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
        String phoneNumber,

        @Schema(description = "Admin's locale", example = "en")
        @NotNull
        @SupportedLocale
        String locale,

        @Schema(description = "Admin's password", example = "StrongP@ss123")
        @NotNull
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "Password must contain at least one digit, one lowercase, one uppercase, one special character and no whitespace"
        )
        String password
) {
}