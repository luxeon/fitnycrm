package com.fitavera.user.service.trainer.mapper;

import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.trainer.model.CreateTrainerRequest;
import com.fitavera.user.rest.trainer.model.UpdateTrainerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface TrainerRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(CreateTrainerRequest request, Locale locale);

    void updateUser(@MappingTarget User user, UpdateTrainerRequest request);
} 