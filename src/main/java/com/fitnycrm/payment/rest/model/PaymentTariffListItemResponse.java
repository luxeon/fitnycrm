package com.fitnycrm.payment.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Payment tariff list item")
public record PaymentTariffListItemResponse(
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
        String currency
) {} 