package com.fitnycrm.user.facade.client.mapper;

import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.rest.client.model.ClientDetailsResponse;
import com.fitnycrm.user.rest.client.model.ClientPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientResponseMapper {

    ClientDetailsResponse toDetailsResponse(User client);

    ClientPageItemResponse toPageItemResponse(User client);
} 