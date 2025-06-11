package com.fitavera.payment.rest.model;

import com.fitavera.common.validation.enumeration.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import com.fitavera.payment.model.Currency;

@Schema(description = "Request to create a new payment tariff")
public record CreatePaymentTariffRequest(
        @Schema(description = "Name of the payment tariff")
        @NotBlank
        String name,

        @Schema(description = "Number of trainings included in this tariff")
        @Min(1)
        @NotNull
        Integer trainingsCount,

        @Schema(description = "Number of days the tariff is valid for")
        @Min(1)
        @NotNull
        Integer validDays,

        @Schema(description = "Price of the tariff")
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price,

        @Schema(description = "Currency code (ISO 4217)")
        @NotNull
        @EnumValue(enumClass = Currency.class)
        String currency
) {
}