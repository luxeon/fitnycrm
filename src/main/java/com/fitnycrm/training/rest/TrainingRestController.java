package com.fitnycrm.training.rest;

import com.fitnycrm.training.facade.TrainingFacade;
import com.fitnycrm.training.rest.model.TrainingRequest;
import com.fitnycrm.training.rest.model.TrainingResponse;
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
@RequestMapping("/api/tenants/{tenantId}/trainings")
@Tag(name = "training", description = "Training management endpoints")
public class TrainingRestController {

    private final TrainingFacade trainingFacade;

    @Operation(summary = "Create a new training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public TrainingResponse create(@PathVariable UUID tenantId,
                                 @RequestBody @Valid TrainingRequest request) {
        return trainingFacade.create(tenantId, request);
    }

    @Operation(summary = "Update an existing training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public TrainingResponse update(@PathVariable UUID tenantId,
                                 @PathVariable UUID id,
                                 @RequestBody @Valid TrainingRequest request) {
        return trainingFacade.update(tenantId, id, request);
    }

    @Operation(summary = "Get training by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training found",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public TrainingResponse findById(@PathVariable UUID tenantId,
                                   @PathVariable UUID id) {
        return trainingFacade.findById(tenantId, id);
    }

    @Operation(summary = "Get paginated list of trainings for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public Page<TrainingResponse> findByTenantId(@PathVariable UUID tenantId,
                                                Pageable pageable) {
        return trainingFacade.findByTenantId(tenantId, pageable);
    }

    @Operation(summary = "Delete a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Training deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Training not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@tenantAccessValidator.check(#tenantId)")
    public void delete(@PathVariable UUID tenantId,
                      @PathVariable UUID id) {
        trainingFacade.delete(tenantId, id);
    }
} 