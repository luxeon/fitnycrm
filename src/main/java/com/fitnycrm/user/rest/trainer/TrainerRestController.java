package com.fitnycrm.user.rest.trainer;

import com.fitnycrm.user.facade.trainer.TrainerFacade;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.rest.trainer.model.TrainerDetailsResponse;
import com.fitnycrm.user.rest.trainer.model.UpdateTrainerRequest;
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
@RequestMapping("/api/tenants/{tenantId}")
@Tag(name = "trainer", description = "Trainer management endpoints")
public class TrainerRestController {

    private final TrainerFacade trainerFacade;

    @Operation(summary = "Create a new trainer account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer account created",
                    content = @Content(schema = @Schema(implementation = TrainerDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PostMapping("/trainers")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public TrainerDetailsResponse create(@PathVariable UUID tenantId,
                                       @RequestBody @Valid CreateTrainerRequest request) {
        return trainerFacade.create(tenantId, request);
    }

    @Operation(summary = "Update an existing trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @PutMapping("/trainers/{trainerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') && @permissionEvaluator.check(#tenantId)")
    public TrainerDetailsResponse update(@PathVariable UUID tenantId,
                                       @PathVariable UUID trainerId,
                                       @RequestBody @Valid UpdateTrainerRequest request) {
        return trainerFacade.update(tenantId, trainerId, request);
    }
} 