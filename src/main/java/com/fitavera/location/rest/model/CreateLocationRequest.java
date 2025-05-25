package com.fitavera.location.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request model for creating or updating a location")
public record CreateLocationRequest(
        @Schema(description = "Street address of the location", example = "123 Main St")
        @NotBlank
        @Size(max = 255)
        String address,
        @Schema(description = "City name", example = "New York")
        @NotBlank
        @Size(max = 100)
        String city,
        @Schema(description = "State or province", example = "NY")
        @NotBlank
        @Size(max = 100)
        String state,
        @Schema(description = "Postal or ZIP code", example = "10001")
        @NotBlank
        @Size(max = 20)
        String postalCode,
        @Schema(description = "Country name", example = "United States")
        @NotBlank
        @Size(max = 50)
        String country,
        @Schema(description = "Timezone in IANA format", example = "America/New_York")
        @NotBlank
        @Size(max = 255)
        String timezone
) {
}