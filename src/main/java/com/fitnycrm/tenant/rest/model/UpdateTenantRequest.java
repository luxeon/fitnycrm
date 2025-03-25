package com.fitnycrm.tenant.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update tenant name")
public record UpdateTenantRequest(
    @Schema(description = "New tenant name", example = "Fitness Club")
    @NotNull
    @Size(min = 1, max = 255)
    String name
) {} 