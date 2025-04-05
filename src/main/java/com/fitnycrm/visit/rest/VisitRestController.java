package com.fitnycrm.visit.rest;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.visit.facade.VisitFacade;
import com.fitnycrm.visit.rest.model.CreateVisitRequest;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations/{locationId}")
public class VisitRestController {

    private final VisitFacade facade;

    @PostMapping("/schedules/{scheduleId}/visits")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    public VisitDetailsResponse register(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                                         @Valid @RequestBody CreateVisitRequest request, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        return facade.register(tenantId, locationId, scheduleId, request, user.getId());
    }

    @DeleteMapping("/schedules/{scheduleId}/visits/{visitId}")
    @PreAuthorize("hasRole('ROLE_CLIENT') && @permissionEvaluator.check(#tenantId)")
    public void cancel(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                       @PathVariable UUID visitId, @AuthenticationPrincipal AuthenticatedUserDetails user) {
        facade.cancel(tenantId, locationId, scheduleId, visitId, user.getId());
    }
}
