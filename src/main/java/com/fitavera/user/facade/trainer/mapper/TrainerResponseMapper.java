package com.fitavera.user.facade.trainer.mapper;

import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.trainer.model.TrainerDetailsResponse;
import com.fitavera.user.rest.trainer.model.TrainerPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerResponseMapper {

    TrainerDetailsResponse toDetailsResponse(User trainer);

    TrainerPageItemResponse toPageItemResponse(User trainer);
} 