package com.fitnycrm.user.rest.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Client page item response")
public record ClientPageItemResponse(
        @Schema(description = "User ID")
        UUID id,

        @Schema(description = "User's first name")
        String firstName,

        @Schema(description = "User's last name")
        String lastName,

        @Schema(description = "User's email")
        String email,

        @Schema(description = "User's phone number")
        String phoneNumber,

        @Schema(description = "Creation timestamp", example = "2024-03-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-03-15T12:00:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
}