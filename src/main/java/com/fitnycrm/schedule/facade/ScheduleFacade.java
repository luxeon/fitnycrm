package com.fitnycrm.schedule.facade;

import com.fitnycrm.schedule.facade.mapper.ScheduleResponseMapper;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import com.fitnycrm.schedule.rest.model.UpdateScheduleRequest;
import com.fitnycrm.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {

    private final ScheduleService scheduleService;
    private final ScheduleResponseMapper responseMapper;

    public ScheduleDetailsResponse create(UUID tenantId, UUID locationId, CreateScheduleRequest request) {
        return responseMapper.toResponse(scheduleService.create(tenantId, locationId, request));
    }

    public ScheduleDetailsResponse update(UUID tenantId, UUID locationId, UUID scheduleId, UpdateScheduleRequest request) {
        return responseMapper.toResponse(scheduleService.update(tenantId, locationId, scheduleId, request));
    }

    public ScheduleDetailsResponse findById(UUID tenantId, UUID locationId, UUID scheduleId) {
        return responseMapper.toResponse(scheduleService.findById(tenantId, locationId, scheduleId));
    }

    public void delete(UUID tenantId, UUID locationId, UUID scheduleId) {
        scheduleService.delete(tenantId, locationId, scheduleId);
    }
} 