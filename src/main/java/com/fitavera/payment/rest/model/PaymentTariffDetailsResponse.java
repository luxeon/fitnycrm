package com.fitavera.payment.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fitavera.payment.model.Currency;

@Schema(description = "Payment tariff details")
public record PaymentTariffDetailsResponse(
        @Schema(description = "Unique identifier of the payment tariff")
        UUID id,

        @Schema(description = "Name of the payment tariff")
        String name,

        @Schema(description = "Number of trainings included in this tariff")
        Integer trainingsCount,

        @Schema(description = "Number of days the tariff is valid for")
        Integer validDays,

        @Schema(description = "Price of the tariff")
        BigDecimal price,

        @Schema(description = "Currency code (ISO 4217)")
        Currency currency,

        @Schema(description = "Timestamp when the tariff was created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @Schema(description = "Timestamp when the tariff was last updated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime updatedAt
) {
}