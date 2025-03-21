package com.fitonyashka.core.admin.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fitonyashka.core.admin.facade.AdminFacade;
import com.fitonyashka.core.admin.rest.model.AdminDetailsResponse;
import com.fitonyashka.core.admin.rest.model.AdminSignupRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "admin", description = "Admin management endpoints")
public class AdminController {

    private final AdminFacade adminFacade;

    @Operation(summary = "Create a new admin account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin account created",
                    content = @Content(schema = @Schema(implementation = AdminDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Admin with this email already exists")
    })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDetailsResponse signup(@RequestBody @Valid AdminSignupRequest request) {
        return adminFacade.signup(request);
    }
} 