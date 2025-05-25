package com.fitavera.payment.rest.model;

import com.fitavera.common.validation.enumeration.EnumValue;
import com.fitavera.payment.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Request to update an existing payment tariff")
public record UpdatePaymentTariffRequest(
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