package com.fitavera.visit.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Schedule view response containing list of schedules with sessions")
public record ScheduleViewResponse(
        @Schema(description = "List of schedules")
        List<ScheduleItem> schedules
) {
    @Schema(description = "Individual schedule with sessions data")
    public record ScheduleItem(
            @Schema(description = "Schedule ID")
            UUID id,

            @Schema(description = "Start time of the schedule")
            LocalTime startTime,

            @Schema(description = "End time of the schedule")
            LocalTime endTime,

            @Schema(description = "Maximum client capacity")
            Integer clientCapacity,

            @Schema(description = "Default trainer for this schedule")
            Trainer defaultTrainer,

            @Schema(description = "Training details")
            Training training,

            @Schema(description = "List of sessions with registration counts")
            List<Session> sessions
    ) {
    }

    @Schema(description = "Trainer information")
    public record Trainer(
            @Schema(description = "Trainer ID")
            UUID id,

            @Schema(description = "Trainer first name")
            String firstName,

            @Schema(description = "Trainer last name")
            String lastName
    ) {
    }

    @Schema(description = "Training information")
    public record Training(
            @Schema(description = "Training ID")
            UUID id,

            @Schema(description = "Training name")
            String name,

            @Schema(description = "Training description")
            String description,

            @Schema(description = "Training duration in minutes")
            Integer durationMinutes
    ) {
    }

    @Schema(description = "Session information with registration count")
    public record Session(
            @Schema(description = "Session date")
            LocalDate date,

            @Schema(description = "Number of registered clients")
            Integer registeredClientsCount
    ) {
    }
} 