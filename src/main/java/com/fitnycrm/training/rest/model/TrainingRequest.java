package com.fitnycrm.training.rest.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrainingRequest(
        @NotNull
        @Size(min = 1, max = 255)
        String name,

        String description,

        @Min(1)
        Integer durationMinutes,

        @Min(1)
        Integer clientCapacity
) {
}