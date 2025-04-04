package com.fitnycrm.payment.facade.mapper;

import com.fitnycrm.payment.repository.entity.ClientTrainingCredit;
import com.fitnycrm.payment.rest.model.ClientTrainingCreditsSummaryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientTrainingCreditMapper {

    ClientTrainingCreditsSummaryResponse toSummaryResponse(ClientTrainingCredit subscription);

}