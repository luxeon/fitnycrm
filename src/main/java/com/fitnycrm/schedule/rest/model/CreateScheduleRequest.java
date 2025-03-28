package com.fitnycrm.schedule.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Request object for creating a new schedule")
public record CreateScheduleRequest(

    @Schema(description = "ID of the location where the training will be held")
    @NotNull
    UUID locationId,

    @Schema(description = "Days of the week when the training occurs")
    @NotNull
    Set<String> daysOfWeek,

    @Schema(description = "Start time of the training")
    @NotNull
    LocalTime startTime,

    @Schema(description = "End time of the training")
    @NotNull
    LocalTime endTime,

    @Schema(description = "ID of the default instructor for this training")
    @NotNull
    UUID defaultInstructorId
) {} 