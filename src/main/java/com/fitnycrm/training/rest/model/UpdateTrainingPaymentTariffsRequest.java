package com.fitnycrm.training.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Request to update training tariffs")
public record UpdateTrainingPaymentTariffsRequest(
    @Schema(description = "List of tariff IDs to assign to the training")
    @NotEmpty
    Set<UUID> tariffIds
) {} 