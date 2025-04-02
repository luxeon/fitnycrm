package com.fitnycrm.payment.rest.model;

import com.fitnycrm.common.validation.EnumValue;
import com.fitnycrm.payment.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request to create a new client payment")
public record CreateClientPaymentRequest(
        @Schema(description = "Number of trainings included in the payment", example = "10")
        @NotNull
        @Positive
        Integer trainingsCount,

        @Schema(description = "Number of days the trainings are valid for", example = "30")
        @NotNull
        @Positive
        Integer validDays,

        @Schema(description = "Price of the payment package", example = "99.99")
        @NotNull
        @Positive
        BigDecimal price,

        @Schema(description = "Currency code for the payment", example = "USD")
        @NotNull
        @EnumValue(enumClass = Currency.class)
        String currency
) {
}