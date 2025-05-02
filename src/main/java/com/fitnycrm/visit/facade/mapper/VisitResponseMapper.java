package com.fitnycrm.visit.facade.mapper;

import com.fitnycrm.visit.repository.entity.Visit;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitResponseMapper {

    @Mapping(source = "schedule.id", target = "scheduleId")
    VisitDetailsResponse toDetailsResponse(Visit register);

}
