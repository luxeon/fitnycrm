package com.fitnycrm.tenant.rest;

import com.fitnycrm.user.service.model.AuthenticatedUserDetails;
import com.fitnycrm.tenant.facade.TenantFacade;
import com.fitnycrm.tenant.rest.model.CreateTenantRequest;
import com.fitnycrm.tenant.rest.model.TenantDetailsResponse;
import com.fitnycrm.tenant.rest.model.UpdateTenantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "tenant")
@RequiredArgsConstructor
@RequestMapping("/api/tenants")
public class TenantRestController {

    private final TenantFacade tenantFacade;

    @Operation(summary = "Create a new tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant created successfully",
                    content = @Content(schema = @Schema(implementation = TenantDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantDetailsResponse create(@AuthenticationPrincipal AuthenticatedUserDetails user,
                                        @RequestBody @Valid CreateTenantRequest request) {
        return tenantFacade.create(user, request);
    }

    @Operation(summary = "Update tenant name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant updated successfully",
                    content = @Content(schema = @Schema(implementation = TenantDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@tenantAccessValidator.check(#id)")
    public TenantDetailsResponse update(@Parameter(description = "ID of the tenant to update", required = true)
                                        @PathVariable UUID id,
                                        @RequestBody @Valid UpdateTenantRequest request) {
        return tenantFacade.update(id, request);
    }

    @Operation(summary = "Get a tenant by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant found",
                    content = @Content(schema = @Schema(implementation = TenantDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@tenantAccessValidator.check(#id)")
    public TenantDetailsResponse getOne(@Parameter(description = "ID of the tenant to retrieve", required = true)
                                        @PathVariable UUID id) {
        return tenantFacade.getById(id);
    }
} 