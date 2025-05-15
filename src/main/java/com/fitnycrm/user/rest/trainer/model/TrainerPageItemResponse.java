package com.fitnycrm.user.rest.trainer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Trainer item response for paginated results")
public record TrainerPageItemResponse(
        @Schema(description = "Unique identifier of the trainer")
        UUID id,

        @Schema(description = "Trainers's first name")
        String firstName,

        @Schema(description = "Trainers's last name")
        String lastName,

        @Schema(description = "Trainers's email address")
        String email,

        @Schema(description = "Trainers's phone number")
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