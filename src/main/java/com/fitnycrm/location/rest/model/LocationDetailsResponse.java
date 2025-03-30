package com.fitnycrm.location.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Detailed location information response")
public record LocationDetailsResponse(
        @Schema(description = "Unique identifier of the location")
        UUID id,
        @Schema(description = "Street address of the location")
        String address,
        @Schema(description = "City where the location is situated")
        String city,
        @Schema(description = "State/Province where the location is situated")
        String state,
        @Schema(description = "Postal/ZIP code of the location")
        String postalCode,
        @Schema(description = "Country where the location is situated")
        String country,
        @Schema(description = "Timezone of the location")
        String timezone,
        @Schema(description = "Timestamp when the location was created")
        OffsetDateTime createdAt,
        @Schema(description = "Timestamp when the location was last updated")
        OffsetDateTime updatedAt
) {
}