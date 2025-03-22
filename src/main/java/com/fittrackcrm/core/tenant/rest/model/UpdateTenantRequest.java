package com.fittrackcrm.core.tenant.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update tenant name")
public record UpdateTenantRequest(
    @Schema(description = "New tenant name", example = "Fitness Club")
    @NotNull(message = "Tenant name is required")
    @Size(min = 1, max = 255, message = "Tenant name must be between 1 and 255 characters")
    String name
) {} 