package com.fitavera.visit.service.dto;

import com.fitavera.training.repository.entity.Training;
import com.fitavera.user.repository.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ScheduleRegistrationSummaryView(
        UUID scheduleId,
        LocalTime startTime,
        LocalTime endTime,
        Integer clientCapacity,
        User trainer,
        Training training,
        List<SessionView> sessions
) {
    public record SessionView(
            LocalDate date,
            Integer registeredClientsCount
    ) {
    }
} 