package com.fitnycrm.training.rest.model;

import java.util.UUID;

public record TrainingResponse(
    UUID id,
    String name,
    String description,
    Integer durationMinutes,
    Integer clientCapacity
) {} 