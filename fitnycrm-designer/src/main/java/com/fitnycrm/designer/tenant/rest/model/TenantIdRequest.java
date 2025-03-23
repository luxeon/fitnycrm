package com.fitnycrm.designer.tenant.rest.model;

import jakarta.validation.constraints.Pattern;

public record TenantIdRequest(
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Invalid UUID format")
    String id
) {} 