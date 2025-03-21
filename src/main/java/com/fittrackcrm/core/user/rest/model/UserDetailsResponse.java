package com.fittrackcrm.core.user.rest.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User details response")
public record UserDetailsResponse(
    @Schema(description = "User ID")
    Long id,

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
) {} 