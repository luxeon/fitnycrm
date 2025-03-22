package com.fittrackcrm.core.security.rest;

import com.fittrackcrm.core.security.facade.AuthFacade;
import com.fittrackcrm.core.security.rest.model.AdminDetailsResponse;
import com.fittrackcrm.core.security.rest.model.AdminSignupRequest;
import com.fittrackcrm.core.security.rest.model.AuthRequest;
import com.fittrackcrm.core.security.rest.model.AuthResponse;
import com.fittrackcrm.core.user.rest.model.UserDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "auth", description = "Authentication endpoints")
public class AuthRestController {

    private final AuthFacade facade;

    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        return facade.authenticate(request);
    }

    @Operation(summary = "Create a new admin account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin account created",
                    content = @Content(schema = @Schema(implementation = UserDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDetailsResponse signup(@RequestBody @Valid AdminSignupRequest request) {
        return facade.signup(request);
    }
} 