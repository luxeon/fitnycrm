package com.fitnycrm.user.service.trainer.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.rest.trainer.model.UpdateTrainerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainerRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(CreateTrainerRequest request);

    void updateUser(@MappingTarget User user, UpdateTrainerRequest request);
} 