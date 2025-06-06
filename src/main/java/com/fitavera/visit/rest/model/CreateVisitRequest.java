package com.fitavera.visit.rest.model;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public record CreateVisitRequest(@FutureOrPresent LocalDate date) {
}
