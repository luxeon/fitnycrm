package com.fitnycrm.payment.facade.mapper;

import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.rest.model.ClientPaymentDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientPaymentMapper {

    ClientPaymentDetailsResponse toDetailsResponse(ClientPayment payment);

} 