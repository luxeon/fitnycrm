package com.fitavera.tenant.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Detailed tenant information")
public record TenantDetailsResponse(
        @Schema(description = "Tenant ID")
        UUID id,

        @Schema(description = "Tenant name")
        String name,

        @Schema(description = "Default tenant locale")
        String locale,

        @Schema(description = "Creation timestamp", example = "2024-10-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-10-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
}