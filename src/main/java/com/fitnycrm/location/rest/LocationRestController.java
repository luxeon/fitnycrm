package com.fitnycrm.location.rest;

import com.fitnycrm.location.facade.LocationFacade;
import com.fitnycrm.location.rest.model.CreateLocationRequest;
import com.fitnycrm.location.rest.model.LocationDetailsResponse;
import com.fitnycrm.location.rest.model.LocationPageItemResponse;
import com.fitnycrm.location.rest.model.UpdateLocationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations")
@Tag(name = "location", description = "Location management endpoints")
public class LocationRestController {

    private final LocationFacade locationFacade;

    @Operation(summary = "Get a page of locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LocationPageItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public Page<LocationPageItemResponse> getAll(@PathVariable UUID tenantId,
                                                 Pageable pageable) {
        return locationFacade.findAll(tenantId, pageable);
    }

    @Operation(summary = "Create a new location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created",
                    content = @Content(schema = @Schema(implementation = LocationDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public LocationDetailsResponse create(@PathVariable UUID tenantId,
                                          @RequestBody @Valid CreateLocationRequest request) {
        return locationFacade.create(tenantId, request);
    }

    @Operation(summary = "Update an existing location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated",
                    content = @Content(schema = @Schema(implementation = LocationDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public LocationDetailsResponse update(@PathVariable UUID tenantId,
                                          @PathVariable UUID id,
                                          @RequestBody @Valid UpdateLocationRequest request) {
        return locationFacade.update(tenantId, id, request);
    }

    @Operation(summary = "Get a location by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LocationDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public LocationDetailsResponse findById(@PathVariable UUID tenantId,
                                            @PathVariable UUID id) {
        return locationFacade.findById(tenantId, id);
    }

    @Operation(summary = "Delete a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public void delete(@PathVariable UUID tenantId,
                       @PathVariable UUID id) {
        locationFacade.delete(tenantId, id);
    }
} 