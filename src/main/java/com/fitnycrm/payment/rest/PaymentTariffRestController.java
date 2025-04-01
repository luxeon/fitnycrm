package com.fitnycrm.payment.rest;

import com.fitnycrm.payment.facade.PaymentTariffFacade;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.rest.model.PaymentTariffResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/trainings/{trainingId}/tariffs")
@RequiredArgsConstructor
@Tag(name = "Payment Tariffs", description = "Payment tariffs management API")
public class PaymentTariffRestController {

    private final PaymentTariffFacade paymentTariffFacade;

    @PostMapping
    @Operation(summary = "Create a new payment tariff")
    public PaymentTariffResponse create(
            @PathVariable UUID tenantId,
            @PathVariable UUID trainingId,
            @RequestBody @Valid CreatePaymentTariffRequest request) {
        return paymentTariffFacade.create(tenantId, trainingId, request);
    }
} 