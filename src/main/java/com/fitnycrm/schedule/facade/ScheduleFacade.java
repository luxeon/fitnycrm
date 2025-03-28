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

    public ScheduleDetailsResponse create(UUID tenantId, UUID trainingId, CreateScheduleRequest request) {
        return responseMapper.toResponse(scheduleService.create(tenantId, trainingId, request));
    }

    public ScheduleDetailsResponse update(UUID tenantId, UUID trainingId, UUID scheduleId, UpdateScheduleRequest request) {
        return responseMapper.toResponse(scheduleService.update(tenantId, trainingId, scheduleId, request));
    }

    public ScheduleDetailsResponse findById(UUID tenantId, UUID trainingId, UUID scheduleId) {
        return responseMapper.toResponse(scheduleService.findById(tenantId, trainingId, scheduleId));
    }

    public void delete(UUID tenantId, UUID trainingId, UUID scheduleId) {
        scheduleService.delete(tenantId, trainingId, scheduleId);
    }
} 