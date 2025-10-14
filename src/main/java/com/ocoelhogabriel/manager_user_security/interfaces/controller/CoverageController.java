package com.ocoelhogabriel.manager_user_security.interfaces.controller;

import com.ocoelhogabriel.manager_user_security.application.usecase.CoverageUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entity.Coverage;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CoverageUpdateRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.mapper.CoverageMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user coverage (abrangência).
 */
@RestController
@RequestMapping("/api/coverages/v1")
@Tag(name = "Coverage", description = "API for managing user access coverage (abrangência)")
@SecurityRequirement(name = "bearerAuth")
public class CoverageController {

    private final CoverageUseCase coverageUseCase;
    private final CoverageMapper coverageMapper;

    @Autowired
    public CoverageController(CoverageUseCase coverageUseCase, CoverageMapper coverageMapper) {
        this.coverageUseCase = coverageUseCase;
        this.coverageMapper = coverageMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new coverage",
        description = "Creates a new coverage for a user to access specific companies/plants",
        responses = {
            @ApiResponse(responseCode = "201", description = "Coverage created successfully",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CoverageResponse> createCoverage(@Valid @RequestBody CoverageRequest request) {
        Coverage coverage = coverageUseCase.createCoverage(
                request.getUserId(),
                request.getCompanyId(),
                request.getPlantId(),
                request.getDescription()
        );
        
        CoverageResponse response = coverageMapper.toResponse(coverage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
        summary = "Get a coverage by ID",
        description = "Returns a coverage based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Coverage found",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Coverage not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CoverageResponse> getCoverage(@PathVariable Long id) {
        Coverage coverage = coverageUseCase.getCoverageById(id);
        CoverageResponse response = coverageMapper.toResponse(coverage);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update a coverage",
        description = "Updates an existing coverage with the provided data",
        responses = {
            @ApiResponse(responseCode = "200", description = "Coverage updated successfully",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Coverage not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<CoverageResponse> updateCoverage(
            @PathVariable Long id,
            @Valid @RequestBody CoverageUpdateRequest request) {
        
        Coverage coverage = coverageUseCase.updateCoverage(
                id,
                request.getCompanyId(),
                request.getPlantId(),
                request.getDescription(),
                request.getActive()
        );
        
        CoverageResponse response = coverageMapper.toResponse(coverage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete a coverage",
        description = "Deletes a coverage based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Coverage deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Coverage not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<Void> deleteCoverage(@PathVariable Long id) {
        coverageUseCase.deleteCoverage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @securityExpression.isCurrentUser(#userId)")
    @Operation(
        summary = "Get coverages by user",
        description = "Returns all coverages for a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of coverages",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CoverageResponse>> getCoveragesByUser(@PathVariable Long userId) {
        List<Coverage> coverages = coverageUseCase.getCoveragesByUser(userId);
        List<CoverageResponse> responses = coverageMapper.toResponseList(coverages);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @securityExpression.isCurrentUser(#userId)")
    @Operation(
        summary = "Get active coverages by user",
        description = "Returns all active coverages for a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of active coverages",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CoverageResponse>> getActiveCoveragesByUser(@PathVariable Long userId) {
        List<Coverage> coverages = coverageUseCase.getActiveCoveragesByUser(userId);
        List<CoverageResponse> responses = coverageMapper.toResponseList(coverages);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
        summary = "Get coverages by company",
        description = "Returns all coverages for a specific company",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of coverages",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CoverageResponse>> getCoveragesByCompany(@PathVariable Long companyId) {
        List<Coverage> coverages = coverageUseCase.getCoveragesByCompany(companyId);
        List<CoverageResponse> responses = coverageMapper.toResponseList(coverages);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/plant/{plantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
        summary = "Get coverages by plant",
        description = "Returns all coverages for a specific plant",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of coverages",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CoverageResponse>> getCoveragesByPlant(@PathVariable Long plantId) {
        List<Coverage> coverages = coverageUseCase.getCoveragesByPlant(plantId);
        List<CoverageResponse> responses = coverageMapper.toResponseList(coverages);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/access/check")
    @PreAuthorize("isAuthenticated()")
    @Operation(
        summary = "Check user access",
        description = "Checks if a user has access to a specific company or plant",
        parameters = {
            @Parameter(name = "userId", description = "User ID"),
            @Parameter(name = "companyId", description = "Company ID (optional)"),
            @Parameter(name = "plantId", description = "Plant ID (optional)")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Access check result"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<Boolean> checkUserAccess(
            @RequestParam Long userId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long plantId) {
        
        boolean hasAccess;
        
        if (companyId != null && plantId == null) {
            // Check company access
            hasAccess = coverageUseCase.hasCompanyAccess(userId, companyId);
        } else if (plantId != null) {
            // Check plant access
            hasAccess = coverageUseCase.hasPlantAccess(userId, plantId);
        } else {
            // Invalid parameters
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(hasAccess);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all coverages",
        description = "Returns all coverages in the system (restricted to admin users)",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of coverages",
                    content = @Content(schema = @Schema(implementation = CoverageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<CoverageResponse>> getAllCoverages() {
        List<Coverage> coverages = coverageUseCase.getAllCoverages();
        List<CoverageResponse> responses = coverageMapper.toResponseList(coverages);
        return ResponseEntity.ok(responses);
    }
}
