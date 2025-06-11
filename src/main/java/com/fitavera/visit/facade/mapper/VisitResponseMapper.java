package com.fitavera.visit.facade.mapper;

import com.fitavera.visit.repository.entity.Visit;
import com.fitavera.visit.rest.model.VisitDetailsResponse;
import com.fitavera.visit.rest.model.VisitPageItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitResponseMapper {

    @Mapping(source = "schedule.id", target = "scheduleId")
    VisitDetailsResponse toDetailsResponse(Visit visit);

    @Mapping(source = "schedule.id", target = "scheduleId")
    VisitPageItemResponse toVisitPageItemResponse(Visit visit);

}
