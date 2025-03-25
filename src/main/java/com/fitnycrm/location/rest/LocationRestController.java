package com.fitnycrm.location.rest;

import com.fitnycrm.location.dto.LocationRequest;
import com.fitnycrm.location.dto.LocationResponse;
import com.fitnycrm.location.facade.LocationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}")
@Tag(name = "location", description = "Location management endpoints")
public class LocationRestController {

    private final LocationFacade locationFacade;

    @Operation(summary = "Create a new location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created",
                    content = @Content(schema = @Schema(implementation = LocationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public LocationResponse create(@PathVariable UUID tenantId,
                                 @RequestBody @Valid LocationRequest request) {
        return locationFacade.create(tenantId, request);
    }
} 