package com.fitavera.payment.service.mapper;

import com.fitavera.payment.repository.entity.PaymentTariff;
import com.fitavera.payment.rest.model.CreatePaymentTariffRequest;
import com.fitavera.payment.rest.model.UpdatePaymentTariffRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentTariffRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PaymentTariff toEntity(CreatePaymentTariffRequest request);

    void update(@MappingTarget PaymentTariff paymentTariff, UpdatePaymentTariffRequest request);
} 