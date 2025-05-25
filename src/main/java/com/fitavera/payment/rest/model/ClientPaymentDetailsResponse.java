package com.fitavera.payment.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitavera.payment.repository.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;

@Schema(description = "Response containing details of a client's payment")
public record ClientPaymentDetailsResponse(
        @Schema(description = "Unique identifier of the payment")
        UUID id,

        @Schema(description = "Current status of the payment")
        PaymentStatus status,

        @Schema(description = "ID of the training")
        UUID trainingId,

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
}