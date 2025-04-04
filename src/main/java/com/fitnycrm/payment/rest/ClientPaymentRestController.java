package com.fitnycrm.payment.rest;

import com.fitnycrm.payment.facade.ClientPaymentFacade;
import com.fitnycrm.payment.rest.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}")
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
    @PostMapping("/clients/{clientId}/payments")
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
    @PostMapping("/clients/{clientId}/payments/cancel")
    @PreAuthorize("(hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId))")
    public ClientPaymentDetailsResponse cancel(@PathVariable UUID tenantId,
                                               @PathVariable UUID clientId,
                                               @PathVariable UUID paymentId) {
        return clientPaymentFacade.cancel(tenantId, clientId, paymentId);
    }

    @Operation(summary = "Find all client payments with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client payments",
                    content = @Content(schema = @Schema(implementation = ClientPaymentPageItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/clients/{clientId}/payments")
    @PreAuthorize("(hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)) or @permissionEvaluator.check(#tenantId, #clientId)")
    public Page<ClientPaymentPageItemResponse> findAll(
            @PathVariable UUID tenantId,
            @PathVariable UUID clientId,
            @Valid ClientPaymentFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return clientPaymentFacade.findAll(tenantId, clientId, filter, pageable);
    }

    @Operation(summary = "Find all payments in tenant with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tenant payments",
                    content = @Content(schema = @Schema(implementation = ClientPaymentPageItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/payments")
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public Page<ClientPaymentPageItemResponse> findAllInTenant(
            @PathVariable UUID tenantId,
            @Valid ExtendedClientPaymentFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return clientPaymentFacade.findAllInTenant(tenantId, filter, pageable);
    }
} 