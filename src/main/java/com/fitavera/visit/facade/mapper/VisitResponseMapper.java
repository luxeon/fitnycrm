package com.fitavera.visit.facade.mapper;

import com.fitavera.visit.repository.entity.Visit;
import com.fitavera.visit.rest.model.ScheduleViewResponse;
import com.fitavera.visit.rest.model.VisitDetailsResponse;
import com.fitavera.visit.rest.model.VisitPageItemResponse;
import com.fitavera.visit.service.dto.ScheduleRegistrationSummaryView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitResponseMapper {

    @Mapping(source = "schedule.id", target = "scheduleId")
    VisitDetailsResponse toDetailsResponse(Visit visit);

    @Mapping(source = "schedule.id", target = "scheduleId")
    VisitPageItemResponse toVisitPageItemResponse(Visit visit);

    default ScheduleViewResponse toScheduleViewResponse(List<ScheduleRegistrationSummaryView> schedules) {
        return new ScheduleViewResponse(schedules.stream().map(this::toScheduleItem).toList());
    }

    @Mapping(target = "id", source = "scheduleId")
    @Mapping(target = "defaultTrainer", source = "trainer")
    ScheduleViewResponse.ScheduleItem toScheduleItem(ScheduleRegistrationSummaryView summary);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    ScheduleViewResponse.Trainer toTrainerResponse(com.fitavera.user.repository.entity.User trainer);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "durationMinutes", source = "durationMinutes")
    ScheduleViewResponse.Training toTrainingResponse(com.fitavera.training.repository.entity.Training training);

    ScheduleViewResponse.Session toSessionResponse(ScheduleRegistrationSummaryView.SessionView session);

}
