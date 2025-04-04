package com.fitnycrm.payment.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Client training credits summary")
public record ClientTrainingCreditsSummaryResponse(

        @Schema(description = "Number of remaining trainings")
        Integer remainingTrainings,

        @Schema(description = "Credits expiration date")
        OffsetDateTime expiresAt
) {
}