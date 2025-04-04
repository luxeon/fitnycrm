package com.fitnycrm.training.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Training item response for paginated results")
public record TrainingPageItemResponse(
        @Schema(description = "Unique identifier of the training")
        UUID id,

        @Schema(description = "Name of the training")
        String name,

        @Schema(description = "Description of the training")
        String description,

        @Schema(description = "Duration in minutes")
        Integer durationMinutes,

        @Schema(description = "Timestamp when the training was created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Timestamp when the training was last updated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
}