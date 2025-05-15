package com.fitnycrm.user.rest.trainer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Trainer details")
public record TrainerDetailsResponse(
        @Schema(description = "Unique identifier of the trainer")
        UUID id,

        @Schema(description = "First name")
        String firstName,

        @Schema(description = "Last name")
        String lastName,

        @Schema(description = "Email address")
        String email,

        @Schema(description = "Phone number")
        String phoneNumber,

        @Schema(description = "Trainers's locale")
        String locale,

        @Schema(description = "Timestamp when the trainer was created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Timestamp when the trainer was last updated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
} 