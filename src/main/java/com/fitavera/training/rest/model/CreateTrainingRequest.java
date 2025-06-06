package com.fitavera.training.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new training")
public record CreateTrainingRequest(
        @Schema(description = "Training name", example = "Yoga Basics")
        @NotNull
        @Size(min = 1, max = 255)
        String name,

        @Schema(description = "Training description", example = "Introduction to basic yoga poses and breathing techniques")
        @Size(min = 1, max = 1000)
        String description,

        @Schema(description = "Duration in minutes", example = "60")
        @NotNull
        @Min(1)
        Integer durationMinutes
) {
}