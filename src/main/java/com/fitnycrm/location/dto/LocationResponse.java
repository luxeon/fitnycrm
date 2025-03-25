package com.fitnycrm.location.dto;

import java.util.UUID;

public record LocationResponse(
        UUID id,
        String name,
        String address,
        String city,
        String state,
        String postalCode,
        String country,
        String timezone
) {} 