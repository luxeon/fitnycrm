package com.fitavera.payment.facade.mapper;

import com.fitavera.payment.repository.entity.PaymentTariff;
import com.fitavera.payment.rest.model.PaymentTariffDetailsResponse;
import com.fitavera.payment.rest.model.PaymentTariffListItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTariffResponseMapper {

    PaymentTariffDetailsResponse toResponse(PaymentTariff paymentTariff);

    PaymentTariffListItemResponse toListItemResponse(PaymentTariff paymentTariff);
} 