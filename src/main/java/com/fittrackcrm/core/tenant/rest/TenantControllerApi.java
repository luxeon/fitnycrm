package com.fittrackcrm.core.tenant.rest;

import com.fittrackcrm.core.tenant.rest.model.TenantDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "tenant")
@RequestMapping("/api/tenants")
public interface TenantControllerApi {

    @Operation(summary = "Get a tenant by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant found",
                    content = @Content(schema = @Schema(implementation = TenantDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    @GetMapping("/{id}")
    TenantDetailsResponse getOne(
            @Parameter(description = "ID of the tenant to retrieve", required = true)
            @PathVariable Long id
    );
} 