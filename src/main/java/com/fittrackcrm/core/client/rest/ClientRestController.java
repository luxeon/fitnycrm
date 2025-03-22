package com.fittrackcrm.core.client.rest;

import com.fittrackcrm.core.client.rest.model.ClientDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fittrackcrm.core.client.facade.ClientFacade;
import com.fittrackcrm.core.client.rest.model.ClientSignupRequest;

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
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDetailsResponse createClient(@PathVariable UUID tenantId,
                                              @RequestBody @Valid ClientSignupRequest request) {
        return clientFacade.createClient(tenantId, request);
    }
} 