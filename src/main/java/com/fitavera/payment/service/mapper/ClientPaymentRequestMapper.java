package com.fitavera.payment.service.mapper;

import com.fitavera.payment.repository.entity.ClientPayment;
import com.fitavera.payment.repository.entity.ClientTrainingCredit;
import com.fitavera.payment.rest.model.CreateClientPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientPaymentRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "training", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "COMPLETED")
    ClientPayment toEntity(CreateClientPaymentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "trigger", constant = "VISIT")
    @Mapping(target = "remainingTrainings", expression = "java(creditsSummary.getRemainingTrainings() - 1)")
    ClientTrainingCredit countVisit(ClientTrainingCredit creditsSummary);
}
