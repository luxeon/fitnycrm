package com.fitavera.visit.rest.model;

import com.fitavera.visit.rest.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange(maxDays = 7)
@Schema(description = "Schedule view request parameters")
public record ScheduleViewRequest(
        @NotNull
        @Schema(description = "Start date for schedule view")
        LocalDate dateFrom,

        @NotNull
        @Schema(description = "End date for schedule view")
        LocalDate dateTo
) {
} 