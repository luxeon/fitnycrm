package com.fitavera.payment.facade.mapper;

import com.fitavera.payment.repository.entity.ClientTrainingCredit;
import com.fitavera.payment.rest.model.ClientTrainingCreditsSummaryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientTrainingCreditMapper {

    ClientTrainingCreditsSummaryResponse toSummaryResponse(ClientTrainingCredit subscription);

}