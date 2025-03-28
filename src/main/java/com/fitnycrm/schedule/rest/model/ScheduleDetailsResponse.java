package com.fitnycrm.schedule.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Response object containing schedule details")
public record ScheduleDetailsResponse(
    @Schema(description = "Unique identifier of the schedule")
    UUID id,

    @Schema(description = "ID of the training this schedule is for")
    UUID trainingId,

    @Schema(description = "ID of the location where the training will be held")
    UUID locationId,

    @Schema(description = "Days of the week when the training occurs")
    Set<String> daysOfWeek,

    @Schema(description = "Start time of the training")
    LocalTime startTime,

    @Schema(description = "End time of the training")
    LocalTime endTime,

    @Schema(description = "ID of the default instructor for this training")
    UUID defaultInstructorId
) {} 