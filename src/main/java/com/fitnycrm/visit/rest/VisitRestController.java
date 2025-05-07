package com.fitnycrm.visit.rest;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.visit.facade.VisitFacade;
import com.fitnycrm.visit.rest.model.CreateVisitRequest;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import com.fitnycrm.visit.rest.model.VisitPageItemResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations/{locationId}")
@Tag(name = "Visits", description = "Visit API")
public class VisitRestController {

    private final VisitFacade facade;

    @PostMapping("/schedules/{scheduleId}/visits")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Register a visit for a schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit registered successfully",
                    content = @Content(schema = @Schema(implementation = VisitDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    public VisitDetailsResponse register(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                                         @Valid @RequestBody CreateVisitRequest request, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        return facade.register(tenantId, locationId, scheduleId, request, user.getId());
    }

    @DeleteMapping("/schedules/{scheduleId}/visits/{visitId}")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Cancel a visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot cancel visit due to business rules"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Visit not found")
    })
    public void cancel(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                       @PathVariable UUID visitId, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        facade.cancel(tenantId, locationId, scheduleId, visitId, user.getId());
    }

    @GetMapping("/visits")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Get all client visits")
    public Page<VisitPageItemResponse> findAll(@PathVariable UUID tenantId, @PathVariable UUID locationId,
                                               @RequestParam(required = false) LocalDate dateFrom,
                                               @RequestParam(required = false) LocalDate dateTo,
                                               @AuthenticationPrincipal AuthenticatedUserDetails user,
                                               Pageable pageable) {
        return facade.findAll(tenantId, locationId, user.getId(), dateFrom, dateTo, pageable);
    }
}
