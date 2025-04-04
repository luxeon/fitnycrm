package com.fitnycrm.payment.facade.mapper;

import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.rest.model.ClientPaymentDetailsResponse;
import com.fitnycrm.payment.rest.model.ClientPaymentPageItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientPaymentMapper {

    @Mapping(target = "trainingId", source = "training.id")
    ClientPaymentDetailsResponse toDetailsResponse(ClientPayment payment);

    @Mapping(target = "trainingId", source = "training.id")
    ClientPaymentPageItemResponse toPageItemResponse(ClientPayment payment);

} 