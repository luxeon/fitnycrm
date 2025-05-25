package com.fitavera.schedule.facade;

import com.fitavera.schedule.facade.mapper.ScheduleResponseMapper;
import com.fitavera.schedule.rest.model.CreateScheduleRequest;
import com.fitavera.schedule.rest.model.ScheduleDetailsResponse;
import com.fitavera.schedule.rest.model.ScheduleListItemResponse;
import com.fitavera.schedule.rest.model.UpdateScheduleRequest;
import com.fitavera.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {

    private final ScheduleService scheduleService;
    private final ScheduleResponseMapper responseMapper;

    public ScheduleDetailsResponse create(UUID tenantId, UUID locationId, CreateScheduleRequest request) {
        return responseMapper.toDetailsResponse(scheduleService.create(tenantId, locationId, request));
    }

    public ScheduleDetailsResponse update(UUID tenantId, UUID locationId, UUID scheduleId, UpdateScheduleRequest request) {
        return responseMapper.toDetailsResponse(scheduleService.update(tenantId, locationId, scheduleId, request));
    }

    public ScheduleDetailsResponse findById(UUID tenantId, UUID locationId, UUID scheduleId) {
        return responseMapper.toDetailsResponse(scheduleService.findById(tenantId, locationId, scheduleId));
    }

    public List<ScheduleListItemResponse> findAll(UUID tenantId, UUID locationId) {
        return scheduleService.findByLocation(tenantId, locationId)
                .stream()
                .map(responseMapper::toListResponse)
                .toList();
    }

    public void delete(UUID tenantId, UUID locationId, UUID scheduleId) {
        scheduleService.delete(tenantId, locationId, scheduleId);
    }
} 