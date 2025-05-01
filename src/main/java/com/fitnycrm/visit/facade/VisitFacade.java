package com.fitnycrm.visit.facade;

import com.fitnycrm.visit.facade.mapper.VisitResponseMapper;
import com.fitnycrm.visit.rest.model.CreateVisitRequest;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import com.fitnycrm.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitFacade {

    private final VisitService service;
    private final VisitResponseMapper responseMapper;

    public VisitDetailsResponse register(UUID tenantId, UUID locationId, UUID scheduleId, CreateVisitRequest request, UUID clientId) {
        return responseMapper.toDetailsResponse(service.register(tenantId, locationId, scheduleId, request, clientId));
    }

    public void cancel(UUID tenantId, UUID locationId, UUID scheduleId, UUID visitId, UUID clientId) {
        service.cancel(scheduleId, visitId, clientId);
    }

    public List<VisitDetailsResponse> findAllByClientId(UUID tenantId, UUID clientId) {
        return service.findAllByClientId(tenantId, clientId).stream()
                .map(responseMapper::toDetailsResponse)
                .toList();
    }
}
