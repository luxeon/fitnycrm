package com.fitavera.visit.service.mapper;

import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.visit.repository.dto.VisitCountView;
import com.fitavera.visit.service.dto.ScheduleRegistrationSummaryView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleRegistrationSummaryMapper {

    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "startTime", source = "schedule.startTime")
    @Mapping(target = "endTime", source = "schedule.endTime")
    @Mapping(target = "clientCapacity", source = "schedule.clientCapacity")
    @Mapping(target = "trainer", source = "schedule.defaultTrainer")
    @Mapping(target = "training", source = "schedule.training")
    @Mapping(target = "sessions", source = "visitCounts")
    ScheduleRegistrationSummaryView toSummaryView(Schedule schedule, List<VisitCountView> visitCounts);

    @Mapping(target = "registeredClientsCount", source = "count")
    ScheduleRegistrationSummaryView.SessionView toSessionView(VisitCountView visitCount);
} 