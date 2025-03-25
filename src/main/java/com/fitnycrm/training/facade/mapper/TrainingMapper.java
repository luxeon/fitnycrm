package com.fitnycrm.training.facade.mapper;

import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.rest.model.CreateTrainingRequest;
import com.fitnycrm.training.rest.model.TrainingDetailsResponse;
import com.fitnycrm.training.rest.model.TrainingPageItemResponse;
import com.fitnycrm.training.rest.model.UpdateTrainingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Training toEntity(UUID tenantId, CreateTrainingRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Training toEntity(UUID tenantId, UpdateTrainingRequest request);

    TrainingDetailsResponse toDetailsResponse(Training training);

    TrainingPageItemResponse toPageItemResponse(Training training);
} 