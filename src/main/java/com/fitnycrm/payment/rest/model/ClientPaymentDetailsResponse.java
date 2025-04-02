package com.fitnycrm.payment.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;

public record ClientPaymentDetailsResponse(
        UUID id,
        PaymentStatus status,
        Integer trainingsCount,
        Integer validDays,
        BigDecimal price,
        Currency currency,

        @Schema(description = "Timestamp when the tariff was last updated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt
) {
}