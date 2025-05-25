package com.fitavera.schedule.facade.mapper;

import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.schedule.rest.model.ScheduleDetailsResponse;
import com.fitavera.schedule.rest.model.ScheduleListItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleDetailsResponse toDetailsResponse(Schedule schedule);

    @Mapping(source = "training.id", target = "trainingId")
    @Mapping(source = "defaultTrainer.id", target = "defaultTrainerId")
    ScheduleListItemResponse toListResponse(Schedule schedule);
} 