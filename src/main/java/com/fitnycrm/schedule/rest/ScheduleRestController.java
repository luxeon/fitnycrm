package com.fitnycrm.schedule.rest;

import com.fitnycrm.schedule.facade.ScheduleFacade;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import com.fitnycrm.schedule.rest.model.ScheduleListItemResponse;
import com.fitnycrm.schedule.rest.model.UpdateScheduleRequest;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations/{locationId}/schedules")
@Tag(name = "schedule", description = "Schedule management endpoints")
public class ScheduleRestController {

    private final ScheduleFacade scheduleFacade;

    @Operation(summary = "Create a new schedule for a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created",
                    content = @Content(schema = @Schema(implementation = ScheduleDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Location or instructor not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ScheduleDetailsResponse create(@PathVariable UUID tenantId,
                                          @PathVariable UUID locationId,
                                          @RequestBody @Valid CreateScheduleRequest request) {
        return scheduleFacade.create(tenantId, locationId, request);
    }

    @Operation(summary = "Update an existing schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule updated successfully",
                    content = @Content(schema = @Schema(implementation = ScheduleDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Schedule or location or instructor not found")
    })
    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ScheduleDetailsResponse update(@PathVariable UUID tenantId,
                                          @PathVariable UUID locationId,
                                          @PathVariable UUID scheduleId,
                                          @RequestBody @Valid UpdateScheduleRequest request) {
        return scheduleFacade.update(tenantId, locationId, scheduleId, request);
    }

    @Operation(summary = "Find schedule by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found",
                    content = @Content(schema = @Schema(implementation = ScheduleDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @GetMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ScheduleDetailsResponse findById(@PathVariable UUID tenantId,
                                            @PathVariable UUID locationId,
                                            @PathVariable UUID scheduleId) {
        return scheduleFacade.findById(tenantId, locationId, scheduleId);
    }

    @Operation(summary = "Get a page of schedules for a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = @Content(schema = @Schema(implementation = ScheduleListItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public List<ScheduleListItemResponse> findAll(@PathVariable UUID tenantId,
                                                  @PathVariable UUID locationId) {
        return scheduleFacade.findAll(tenantId, locationId);
    }

    @Operation(summary = "Delete a schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Schedule deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public void delete(@PathVariable UUID tenantId,
                       @PathVariable UUID locationId,
                       @PathVariable UUID scheduleId) {
        scheduleFacade.delete(tenantId, locationId, scheduleId);
    }
} 