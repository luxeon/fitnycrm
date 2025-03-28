package com.fitnycrm.schedule.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.time.OffsetDateTime;
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

    @Schema(description = "ID of the default trainer for this training")
    UUID defaultTrainerId,

    @Schema(description = "Timestamp when the schedule was created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime createdAt,

    @Schema(description = "Timestamp when the schedule was last updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime updatedAt
) {} 