package com.fitnycrm.visit.facade.mapper;

import com.fitnycrm.visit.repository.entity.Visit;
import com.fitnycrm.visit.rest.model.VisitDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitResponseMapper {

    VisitDetailsResponse toDetailsResponse(Visit register);

}
