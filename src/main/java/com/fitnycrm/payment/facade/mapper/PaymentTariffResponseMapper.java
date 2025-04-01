package com.fitnycrm.payment.facade.mapper;

import com.fitnycrm.payment.repository.entity.PaymentTariff;
import com.fitnycrm.payment.rest.model.PaymentTariffResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentTariffResponseMapper {

    @Mapping(target = "trainingId", source = "training.id")
    PaymentTariffResponse toResponse(PaymentTariff paymentTariff);
} 