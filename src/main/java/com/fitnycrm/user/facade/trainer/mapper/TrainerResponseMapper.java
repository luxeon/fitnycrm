package com.fitnycrm.user.facade.trainer.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.trainer.model.TrainerDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerResponseMapper {

    TrainerDetailsResponse toDetailsResponse(User trainer);
} 