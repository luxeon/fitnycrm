package com.fitavera.training.service.mapper;

import com.fitavera.training.repository.entity.Training;
import com.fitavera.training.rest.model.CreateTrainingRequest;
import com.fitavera.training.rest.model.UpdateTrainingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Training toTraining(CreateTrainingRequest request);

    Training update(@MappingTarget Training training, UpdateTrainingRequest request);
} 