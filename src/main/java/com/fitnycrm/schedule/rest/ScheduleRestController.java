package com.fitnycrm.schedule.rest;

import com.fitnycrm.schedule.facade.ScheduleFacade;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
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
@RequestMapping("/api/tenants/{tenantId}/trainings/{trainingId}/schedules")
@Tag(name = "schedule", description = "Schedule management endpoints")
public class ScheduleRestController {

    private final ScheduleFacade scheduleFacade;

    @Operation(summary = "Create a new schedule for a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created",
                    content = @Content(schema = @Schema(implementation = ScheduleDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Training or location or instructor not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ScheduleDetailsResponse create(@PathVariable UUID tenantId,
                                          @PathVariable UUID trainingId,
                                          @RequestBody @Valid CreateScheduleRequest request) {
        return scheduleFacade.create(tenantId, trainingId, request);
    }
} 