package com.fitavera.location.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Location item response for paginated results")
public record LocationPageItemResponse(
        @Schema(description = "Unique identifier of the location")
        UUID id,

        @Schema(description = "Street address of the location")
        String address,

        @Schema(description = "City of the location")
        String city,

        @Schema(description = "State/Province of the location")
        String state,

        @Schema(description = "Postal/ZIP code of the location")
        String postalCode,

        @Schema(description = "Country of the location")
        String country,

        @Schema(description = "Timezone of the location")
        String timezone,

        @Schema(description = "Timestamp when the location was created")
        OffsetDateTime createdAt,

        @Schema(description = "Timestamp when the location was last updated")
        OffsetDateTime updatedAt) {
}
