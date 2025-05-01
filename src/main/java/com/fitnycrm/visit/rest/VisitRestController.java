package com.fitnycrm.visit.rest;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.visit.facade.VisitFacade;
import com.fitnycrm.visit.rest.model.CreateVisitRequest;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations/{locationId}")
@Tag(name = "Visits", description = "API for managing client visits")
public class VisitRestController {

    private final VisitFacade facade;

    @PostMapping("/schedules/{scheduleId}/visits")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Register a new visit")
    public VisitDetailsResponse register(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                                         @Valid @RequestBody CreateVisitRequest request, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        return facade.register(tenantId, locationId, scheduleId, request, user.getId());
    }

    @DeleteMapping("/schedules/{scheduleId}/visits/{visitId}")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Cancel a visit")
    public void cancel(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                       @PathVariable UUID visitId, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        facade.cancel(tenantId, locationId, scheduleId, visitId, user.getId());
    }

    @GetMapping("/visits")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    @Operation(summary = "Get all client visits")
    public List<VisitDetailsResponse> findAll(@PathVariable UUID tenantId, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        return facade.findAllByClientId(tenantId, user.getId());
    }
}
