package com.fitnycrm.payment.service.mapper;

import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientPaymentRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "COMPLETED")
    ClientPayment toEntity(CreateClientPaymentRequest request);
}
