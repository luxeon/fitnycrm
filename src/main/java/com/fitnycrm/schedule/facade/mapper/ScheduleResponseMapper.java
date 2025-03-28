package com.fitnycrm.schedule.facade.mapper;

import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.schedule.rest.model.ScheduleDetailsResponse;
import com.fitnycrm.schedule.rest.model.SchedulePageItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleDetailsResponse toDetailsResponse(Schedule schedule);

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    SchedulePageItemResponse toListResponse(Schedule schedule);
} 