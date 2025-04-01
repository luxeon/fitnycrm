package com.fitnycrm.payment.rest;

import com.fitnycrm.payment.facade.PaymentTariffFacade;
import com.fitnycrm.payment.rest.model.CreatePaymentTariffRequest;
import com.fitnycrm.payment.rest.model.PaymentTariffResponse;
import com.fitnycrm.payment.rest.model.UpdatePaymentTariffRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment tariff created successfully",
                    content = @Content(schema = @Schema(implementation = PaymentTariffResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public PaymentTariffResponse create(
            @PathVariable UUID tenantId,
            @PathVariable UUID trainingId,
            @RequestBody @Valid CreatePaymentTariffRequest request) {
        return paymentTariffFacade.create(tenantId, trainingId, request);
    }

    @PutMapping("/{tariffId}")
    @Operation(summary = "Update an existing payment tariff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment tariff updated successfully",
                    content = @Content(schema = @Schema(implementation = PaymentTariffResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Payment tariff or training not found"),
            @ApiResponse(responseCode = "409", description = "Payment tariff does not belong to the specified training")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public PaymentTariffResponse update(
            @PathVariable UUID tenantId,
            @PathVariable UUID trainingId,
            @PathVariable UUID tariffId,
            @RequestBody @Valid UpdatePaymentTariffRequest request) {
        return paymentTariffFacade.update(tenantId, trainingId, tariffId, request);
    }

    @DeleteMapping("/{tariffId}")
    @Operation(summary = "Delete a payment tariff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment tariff deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Payment tariff not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public void delete(
            @PathVariable UUID tenantId,
            @PathVariable UUID trainingId,
            @PathVariable UUID tariffId) {
        paymentTariffFacade.delete(tenantId, trainingId, tariffId);
    }
} 