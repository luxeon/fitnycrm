package com.fitnycrm.location.rest.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LocationResponse(
        UUID id,
        String address,
        String city,
        String state,
        String postalCode,
        String country,
        String timezone,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {} 