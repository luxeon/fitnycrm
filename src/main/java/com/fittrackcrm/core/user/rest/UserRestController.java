package com.fittrackcrm.core.user.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fittrackcrm.core.user.facade.UserFacade;
import com.fittrackcrm.core.user.rest.model.UserDetailsResponse;
import com.fittrackcrm.core.user.rest.model.UserSignupRequest;

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
@RequestMapping("/api/tenants/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "user", description = "User management endpoints")
public class UserRestController {

    private final UserFacade userFacade;

    @Operation(summary = "Create a new client account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client account created",
                    content = @Content(schema = @Schema(implementation = UserDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsResponse createClient(@PathVariable UUID tenantId,
                                            @RequestBody @Valid UserSignupRequest request) {
        return userFacade.signup(tenantId, request);
    }
} 