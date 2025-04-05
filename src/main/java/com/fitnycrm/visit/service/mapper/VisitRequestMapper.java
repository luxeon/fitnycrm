package com.fitnycrm.visit.service.mapper;

import com.fitnycrm.visit.repository.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface VisitRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "REGISTERED")
    Visit toEntity(LocalDate date);
}
