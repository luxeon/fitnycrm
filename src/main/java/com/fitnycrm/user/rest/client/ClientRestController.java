package com.fitnycrm.user.rest.client;

import com.fitnycrm.user.facade.client.ClientFacade;
import com.fitnycrm.user.rest.client.model.*;
import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/clients")
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
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ClientDetailsResponse create(@PathVariable UUID tenantId,
                                        @RequestBody @Valid CreateClientRequest request) {
        return clientFacade.create(tenantId, request);
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
    @PutMapping("/{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public ClientDetailsResponse update(@PathVariable UUID tenantId,
                                        @PathVariable UUID clientId,
                                        @RequestBody @Valid UpdateClientRequest request) {
        return clientFacade.update(tenantId, clientId, request);
    }

    @Operation(summary = "Get client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(schema = @Schema(implementation = ClientDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{clientId}")
    @PreAuthorize("((hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)) or @permissionEvaluator.check(#tenantId, #clientId))")
    public ClientDetailsResponse findById(@PathVariable UUID tenantId,
                                          @PathVariable UUID clientId) {
        return clientFacade.findById(tenantId, clientId);
    }

    @Operation(summary = "Get paginated list of clients for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ClientPageItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public Page<ClientPageItemResponse> findByTenantId(@PathVariable UUID tenantId,
                                                       Pageable pageable) {
        return clientFacade.findByTenantId(tenantId, pageable);
    }

    @Operation(summary = "Delete a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public void delete(@PathVariable UUID tenantId,
                       @PathVariable UUID clientId) {
        clientFacade.delete(tenantId, clientId);
    }

    @Operation(summary = "Invite a new client by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Invitation sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/invite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_TRAINER', 'ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public void invite(@PathVariable UUID tenantId,
                       @RequestBody @Valid InviteClientRequest request,
                       @AuthenticationPrincipal AuthenticatedUserDetails user) {
        clientFacade.invite(tenantId, request, user);
    }

    @Operation(summary = "Sign up a new client using invitation token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client signed up successfully",
                    content = @Content(schema = @Schema(implementation = ClientDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Invitation not found or expired")
    })
    @PostMapping("/signup/{token}")
    public ClientDetailsResponse signup(@PathVariable UUID tenantId,
                                        @PathVariable String token,
                                        @RequestBody @Valid SignupClientRequest request) {
        return clientFacade.signup(tenantId, token, request);
    }
} 