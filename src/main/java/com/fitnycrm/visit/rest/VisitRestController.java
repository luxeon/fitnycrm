package com.fitnycrm.visit.rest;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.visit.facade.VisitFacade;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/locations/{locationId}")
public class VisitRestController {

    private final VisitFacade facade;

    @PostMapping("/schedules/{scheduleId}/{date}/register")
    public VisitDetailsResponse register(@PathVariable UUID tenantId, @PathVariable UUID locationId, @PathVariable UUID scheduleId,
                                         @PathVariable @FutureOrPresent LocalDate date, @AuthenticationPrincipal AuthenticatedUserDetails user) {
         return facade.register(tenantId, locationId, scheduleId, date, user.getId());
    }

}
