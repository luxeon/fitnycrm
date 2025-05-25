package com.fitavera.user.facade.client.mapper;

import com.fitavera.user.repository.entity.User;
import com.fitavera.user.rest.client.model.ClientDetailsResponse;
import com.fitavera.user.rest.client.model.ClientPageItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientResponseMapper {

    ClientDetailsResponse toDetailsResponse(User client);

    ClientPageItemResponse toPageItemResponse(User client);
} 