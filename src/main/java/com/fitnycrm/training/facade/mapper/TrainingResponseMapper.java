package com.fitnycrm.training.facade.mapper;

import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.training.rest.model.TrainingDetailsResponse;
import com.fitnycrm.training.rest.model.TrainingPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingResponseMapper {

    TrainingDetailsResponse toDetailsResponse(Training training);

    TrainingPageItemResponse toPageItemResponse(Training training);
} 