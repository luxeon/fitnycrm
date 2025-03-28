package com.fitnycrm.schedule.facade.mapper;

import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleDetailsResponse toResponse(Schedule schedule);
} 