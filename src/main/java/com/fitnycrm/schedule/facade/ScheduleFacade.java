package com.fitnycrm.schedule.facade;

import com.fitnycrm.schedule.facade.mapper.ScheduleResponseMapper;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import com.fitnycrm.schedule.rest.model.SchedulePageItemResponse;
import com.fitnycrm.schedule.rest.model.UpdateScheduleRequest;
import com.fitnycrm.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<SchedulePageItemResponse> findAll(UUID tenantId, UUID locationId, Pageable pageable) {
        return scheduleService.findByLocation(tenantId, locationId, pageable)
                .map(responseMapper::toListResponse);
    }

    public void delete(UUID tenantId, UUID locationId, UUID scheduleId) {
        scheduleService.delete(tenantId, locationId, scheduleId);
    }
} 