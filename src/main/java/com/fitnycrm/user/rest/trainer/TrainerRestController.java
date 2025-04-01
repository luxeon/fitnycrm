package com.fitnycrm.user.rest.trainer;

import com.fitnycrm.user.facade.trainer.TrainerFacade;
import com.fitnycrm.user.rest.trainer.model.CreateTrainerRequest;
import com.fitnycrm.user.rest.trainer.model.TrainerDetailsResponse;
import com.fitnycrm.user.rest.trainer.model.TrainerPageItemResponse;
import com.fitnycrm.user.rest.trainer.model.UpdateTrainerRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenants/{tenantId}/trainers")
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
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
    @PutMapping("/{trainerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public TrainerDetailsResponse update(@PathVariable UUID tenantId,
                                         @PathVariable UUID trainerId,
                                         @RequestBody @Valid UpdateTrainerRequest request) {
        return trainerFacade.update(tenantId, trainerId, request);
    }

    @Operation(summary = "Delete a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @DeleteMapping("/{trainerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public void delete(@PathVariable UUID tenantId,
                       @PathVariable UUID trainerId) {
        trainerFacade.delete(tenantId, trainerId);
    }

    @Operation(summary = "Get trainer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found",
                    content = @Content(schema = @Schema(implementation = TrainerDetailsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{trainerId}")
    @PreAuthorize("((hasRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)) or @permissionEvaluator.check(#tenantId, #trainerId))")
    public TrainerDetailsResponse findById(@PathVariable UUID tenantId,
                                           @PathVariable UUID trainerId) {
        return trainerFacade.findById(tenantId, trainerId);
    }

    @Operation(summary = "Get paginated list of trainers for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainers retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainerPageItemResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and @permissionEvaluator.check(#tenantId)")
    public Page<TrainerPageItemResponse> findByTenantId(@PathVariable UUID tenantId,
                                                        Pageable pageable) {
        return trainerFacade.findByTenantId(tenantId, pageable);
    }
} 