package com.fitavera.training.facade.mapper;

import com.fitavera.training.repository.entity.Training;
import com.fitavera.training.rest.model.TrainingDetailsResponse;
import com.fitavera.training.rest.model.TrainingPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingResponseMapper {

    TrainingDetailsResponse toDetailsResponse(Training training);

    TrainingPageItemResponse toPageItemResponse(Training training);
} 