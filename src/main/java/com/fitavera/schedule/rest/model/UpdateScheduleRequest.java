package com.fitavera.schedule.rest.model;

import com.fitavera.common.validation.enumeration.EnumSetValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Request object for updating an existing schedule")
public record UpdateScheduleRequest(

        @Schema(description = "Days of the week when the training occurs")
        @NotNull
        @EnumSetValue(enumClass = DayOfWeek.class)
        Set<String> daysOfWeek,

        @Schema(description = "Start time of the training")
        @NotNull
        LocalTime startTime,

        @Schema(description = "End time of the training")
        @NotNull
        LocalTime endTime,

        @Schema(description = "ID of the default trainer for this training")
        @NotNull
        UUID defaultTrainerId,

        @Schema(description = "Maximum number of clients", example = "20")
        @NotNull
        @Min(1)
        Integer clientCapacity
) {
}