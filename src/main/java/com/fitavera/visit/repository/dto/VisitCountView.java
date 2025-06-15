package com.fitavera.visit.repository.dto;

import java.time.LocalDate;
import java.util.UUID;

public record VisitCountView(
        UUID scheduleId,
        LocalDate date,
        Long count
) {
} 