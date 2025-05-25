package com.fitavera.visit.facade;

import com.fitavera.visit.facade.mapper.VisitResponseMapper;
import com.fitavera.visit.rest.model.CreateVisitRequest;
import com.fitavera.visit.rest.model.VisitDetailsResponse;
import com.fitavera.visit.rest.model.VisitPageItemResponse;
import com.fitavera.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public Page<VisitPageItemResponse> findAll(UUID tenantId, UUID locationId, UUID clientId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        return service.findAll(tenantId, locationId, clientId, dateFrom, dateTo, pageable)
                .map(responseMapper::toVisitPageItemResponse);
    }
}
