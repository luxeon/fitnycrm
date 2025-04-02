package com.fitnycrm.payment.rest.model;

import com.fitnycrm.common.validation.EnumValue;
import com.fitnycrm.payment.model.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateClientPaymentRequest(
        @NotNull
        @Positive
        Integer trainingsCount,

        @NotNull
        @Positive
        Integer validDays,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @EnumValue(enumClass = Currency.class)
        String currency
) {
}