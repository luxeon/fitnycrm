package com.fitavera.schedule.service.mapper;

import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.schedule.rest.model.CreateScheduleRequest;
import com.fitavera.schedule.rest.model.UpdateScheduleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScheduleRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "training", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "defaultTrainer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Schedule toSchedule(CreateScheduleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "training", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "defaultTrainer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget Schedule schedule, UpdateScheduleRequest request);
} 