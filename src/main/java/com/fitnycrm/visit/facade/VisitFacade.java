package com.fitnycrm.visit.facade;

import com.fitnycrm.visit.facade.mapper.VisitResponseMapper;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import com.fitnycrm.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitFacade {

    private final VisitService service;
    private final VisitResponseMapper responseMapper;

    public VisitDetailsResponse register(UUID tenantId, UUID locationId, UUID scheduleId, LocalDate date, UUID clientId) {
        return responseMapper.toDetailsResponse(service.register(tenantId, locationId, scheduleId, date, clientId));
    }
}
