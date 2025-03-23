package com.fitnycrm.designer.user.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Admin details response")
public record UserDetailsResponse(
    @Schema(description = "Admin ID")
    UUID id,

    @Schema(description = "Admin's first name")
    String firstName,

    @Schema(description = "Admin's last name")
    String lastName,

    @Schema(description = "Admin's email")
    String email,

    @Schema(description = "Admin's phone number")
    String phoneNumber,

    @Schema(description = "Creation timestamp", example = "2024-03-15T12:00:00Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime createdAt,

    @Schema(description = "Last update timestamp", example = "2024-03-15T12:00:00Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime updatedAt
) {} 