package com.fitavera.user.rest.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Client page item response")
public record ClientPageItemResponse(
        @Schema(description = "Client ID")
        UUID id,

        @Schema(description = "Client's first name")
        String firstName,

        @Schema(description = "Client's last name")
        String lastName,

        @Schema(description = "Client's email")
        String email,

        @Schema(description = "Client's phone number")
        String phoneNumber,

        @Schema(description = "Client's locale")
        String locale,

        @Schema(description = "Creation timestamp", example = "2024-03-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-03-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
}