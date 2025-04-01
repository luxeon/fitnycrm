package com.fitnycrm.payment.facade.mapper;

import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.payment.rest.model.PaymentTariffDetailsResponse;
import com.fitnycrm.payment.rest.model.PaymentTariffListItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTariffResponseMapper {

    PaymentTariffDetailsResponse toResponse(PaymentTariff paymentTariff);

    PaymentTariffListItemResponse toListItemResponse(PaymentTariff paymentTariff);
} 