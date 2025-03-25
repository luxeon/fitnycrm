package com.fitnycrm.client.rest;

import com.fitnycrm.client.rest.model.ClientDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fitnycrm.client.facade.ClientFacade;
import com.fitnycrm.client.rest.model.ClientSignupRequest;
import com.fitnycrm.client.rest.model.ClientUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}")
@Tag(name = "client", description = "Client management endpoints")
public class ClientRestController {

    private final ClientFacade clientFacade;

    @Operation(summary = "Create a new client account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client account created",
                    content = @Content(schema = @Schema(implementation = ClientDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public ClientDetailsResponse create(@PathVariable UUID tenantId,
                                              @RequestBody @Valid ClientSignupRequest request) {
        return clientFacade.createClient(tenantId, request);
    }

    @Operation(summary = "Update an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully",
                    content = @Content(schema = @Schema(implementation = ClientDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PutMapping("/clients/{clientId}")
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public ClientDetailsResponse update(@PathVariable UUID tenantId,
                                      @PathVariable UUID clientId,
                                      @RequestBody @Valid ClientUpdateRequest request) {
        return clientFacade.updateClient(tenantId, clientId, request);
    }
} 