package com.fitnycrm.payment.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Request to create a new payment tariff")
public record CreatePaymentTariffRequest(
        @Schema(description = "Name of the payment tariff")
        @NotBlank
        String name,

        @Schema(description = "Number of trainings included in this tariff")
        @Min(1)
        Integer trainingsCount,

        @Schema(description = "Number of days the tariff is valid for")
        @Min(1)
        Integer validDays,

        @Schema(description = "Price of the tariff")
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price,

        @Schema(description = "Currency code (ISO 4217)")
        @NotNull
        @Size(min = 3, max = 3)
        String currency
) {
}