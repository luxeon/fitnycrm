package com.fitnycrm.schedule.facade.mapper;

import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import com.fitnycrm.schedule.rest.model.ScheduleListItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleDetailsResponse toDetailsResponse(Schedule schedule);

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleListItemResponse toListResponse(Schedule schedule);
} 