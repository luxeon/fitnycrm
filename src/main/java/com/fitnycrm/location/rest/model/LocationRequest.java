package com.fitnycrm.location.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank
        @Size(max = 255)
        String address,
        @NotBlank
        @Size(max = 100)
        String city,
        @NotBlank
        @Size(max = 100)
        String state,
        @NotBlank
        @Size(max = 20)
        String postalCode,
        @NotBlank
        @Size(max = 50)
        String country,
        @NotBlank
        @Size(max = 255)
        String timezone
) {} 