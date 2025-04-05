package com.fitnycrm.visit.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VisitDetailsResponse(UUID id,
                                   UUID scheduleId,
                                   LocalDate date,
                                   @Schema(description = "Timestamp when the location was created")
                                   OffsetDateTime createdAt,
                                   @Schema(description = "Timestamp when the location was last updated")
                                   OffsetDateTime updatedAt) {
}
