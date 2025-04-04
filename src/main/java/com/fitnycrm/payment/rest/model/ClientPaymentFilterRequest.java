package com.fitnycrm.payment.rest.model;

import com.fitnycrm.common.validation.EnumValue;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Filter parameters for client payments")
public record ClientPaymentFilterRequest(
        @Schema(description = "Filter by payment status")
        @EnumValue(enumClass = PaymentStatus.class)
        String status,

        @Schema(description = "Filter by training ID")
        UUID trainingId,

        @Schema(description = "Filter by created date from (inclusive)", example = "2024-01-01T00:00:00Z")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime createdAtFrom,

        @Schema(description = "Filter by created date to (inclusive)", example = "2024-12-31T23:59:59Z")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime createdAtTo
) {
} 