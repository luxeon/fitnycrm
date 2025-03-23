package com.fitnycrm.designer.user.rest;

import com.fitnycrm.designer.user.facade.AuthFacade;
import com.fitnycrm.designer.user.rest.model.UserDetailsResponse;
import com.fitnycrm.designer.user.rest.model.UserSignupRequest;
import com.fitnycrm.designer.user.rest.model.AuthRequest;
import com.fitnycrm.designer.user.rest.model.AuthResponse;
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
    public UserDetailsResponse signup(@RequestBody @Valid UserSignupRequest request) {
        return facade.signup(request);
    }

    @Operation(summary = "Confirm email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String token) {
        facade.confirmEmail(token);
    }
} 