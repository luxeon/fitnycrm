package com.fitavera.payment.facade.mapper;

import com.fitavera.payment.repository.entity.ClientPayment;
import com.fitavera.payment.rest.model.ClientPaymentDetailsResponse;
import com.fitavera.payment.rest.model.ClientPaymentPageItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientPaymentMapper {

    @Mapping(target = "trainingId", source = "training.id")
    ClientPaymentDetailsResponse toDetailsResponse(ClientPayment payment);

    ClientPaymentPageItemResponse toPageItemResponse(ClientPayment payment);

} 