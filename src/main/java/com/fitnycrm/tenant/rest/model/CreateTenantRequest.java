package com.fitnycrm.tenant.rest.model;

import com.fitnycrm.common.validation.locale.SupportedLocale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new tenant with admin user")
public record CreateTenantRequest(
        @Schema(description = "Tenant name", example = "Fitness Club")
        @NotNull
        @Size(min = 1, max = 255)
        String name,

        @Schema(description = "Default tenant language", example = "en")
        @NotNull
        @SupportedLocale
        String locale
) {
} 