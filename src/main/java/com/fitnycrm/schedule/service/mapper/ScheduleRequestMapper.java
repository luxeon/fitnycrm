package com.fitnycrm.schedule.service.mapper;

import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.CreateScheduleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "training", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "defaultTrainer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Schedule toSchedule(CreateScheduleRequest request);
} 