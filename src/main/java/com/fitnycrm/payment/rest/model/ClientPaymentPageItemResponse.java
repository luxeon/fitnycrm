package com.fitnycrm.payment.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitnycrm.payment.repository.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;

@Schema(description = "Client payment item response for paginated results")
public record ClientPaymentPageItemResponse(
        @Schema(description = "Unique identifier of the payment")
        UUID id,

        @Schema(description = "Current status of the payment")
        PaymentStatus status,

        @Schema(description = "Payment training")
        Training training,

        @Schema(description = "Number of trainings included in the payment")
        Integer trainingsCount,

        @Schema(description = "Number of days the payment is valid for")
        Integer validDays,

        @Schema(description = "Price amount of the payment")
        BigDecimal price,

        @Schema(description = "Currency of the payment")
        Currency currency,

        @Schema(description = "Timestamp when the payment was created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt
) {

    public record Training(@Schema(description = "Id of the training") UUID id,
                           @Schema(description = "Name of the training") String name) {
    }
}
