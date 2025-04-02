package com.fitnycrm.payment.rest;

import com.fitnycrm.payment.facade.ClientPaymentFacade;
import com.fitnycrm.payment.rest.model.ClientPaymentDetailsResponse;
import com.fitnycrm.payment.rest.model.CreateClientPaymentRequest;
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
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/clients/{clientId}/payments")
@Tag(name = "client-payment", description = "Client payment management endpoints")
public class ClientPaymentRestController {

    private final ClientPaymentFacade clientPaymentFacade;

    @Operation(summary = "Create a new client payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully",
                    content = @Content(schema = @Schema(implementation = ClientPaymentDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("(hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId))")
    public ClientPaymentDetailsResponse create(@PathVariable UUID tenantId,
                                               @PathVariable UUID clientId,
                                               @RequestBody @Valid CreateClientPaymentRequest request) {
        return clientPaymentFacade.create(tenantId, clientId, request);
    }

    @Operation(summary = "Cancel a client payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment canceled successfully",
                    content = @Content(schema = @Schema(implementation = ClientPaymentDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{paymentId}/cancel")
    @PreAuthorize("(hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId))")
    public ClientPaymentDetailsResponse cancel(@PathVariable UUID tenantId,
                                             @PathVariable UUID clientId,
                                             @PathVariable UUID paymentId) {
        return clientPaymentFacade.cancel(tenantId, clientId, paymentId);
    }
} 