package com.fittrackcrm.core.tenant.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new tenant with admin user")
public record CreateTenantRequest(
    @Schema(description = "Tenant name", example = "Fitness Club")
    @NotBlank(message = "Tenant name is required")
    @Size(min = 1, max = 255, message = "Tenant name must be between 1 and 255 characters")
    String name
) {
} 