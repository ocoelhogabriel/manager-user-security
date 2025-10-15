package com.ocoelhogabriel.manager_user_security.interfaces.controllers;

import com.ocoelhogabriel.manager_user_security.domain.entity.Plant;
import com.ocoelhogabriel.manager_user_security.domain.service.PlantService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.PlantUpdateRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.PlantMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants/v1")
@Tag(name = "Plants", description = "API for plant management")
@SecurityRequirement(name = "bearerAuth")
public class PlantController {

    private final PlantService plantService;
    private final PlantMapper plantMapper;

    public PlantController(PlantService plantService, PlantMapper plantMapper) {
        this.plantService = plantService;
        this.plantMapper = plantMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new plant",
        description = "Creates a new plant with the provided details",
        responses = {
            @ApiResponse(responseCode = "201", description = "Plant created successfully",
                    content = @Content(schema = @Schema(implementation = PlantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Plant with same name and company already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<PlantResponse> createPlant(@Valid @RequestBody PlantRequest request) {
        Plant plant = plantMapper.toDomain(request);
        Plant createdPlant = plantService.registerPlant(plant);
        PlantResponse response = plantMapper.toResponse(createdPlant);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update an existing plant",
        description = "Updates an existing plant with the provided details",
        responses = {
            @ApiResponse(responseCode = "200", description = "Plant updated successfully",
                    content = @Content(schema = @Schema(implementation = PlantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "409", description = "Plant with same name and company already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<PlantResponse> updatePlant(@Valid @RequestBody PlantUpdateRequest request) {
        Plant plant = plantMapper.toDomain(request);
        Plant updatedPlant = plantService.updatePlant(plant);
        PlantResponse response = plantMapper.toResponse(updatedPlant);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get a plant by ID",
        description = "Returns a plant based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Plant found",
                    content = @Content(schema = @Schema(implementation = PlantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<PlantResponse> getPlant(@PathVariable Long id) {
        Plant plant = plantService.getPlantById(id);
        PlantResponse response = plantMapper.toResponse(plant);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get plants by company ID",
        description = "Returns a list of plants for a specific company",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of plants",
                    content = @Content(schema = @Schema(implementation = PlantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<PlantResponse>> getPlantsByCompanyId(@PathVariable Long companyId) {
        List<Plant> plants = plantService.findByCompanyId(companyId);
        List<PlantResponse> responses = plantMapper.toResponseList(plants);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
        summary = "Get all plants",
        description = "Returns a list of all plants",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of plants",
                    content = @Content(schema = @Schema(implementation = PlantResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<List<PlantResponse>> getAllPlants() {
        List<Plant> plants = plantService.findAllPlants();
        List<PlantResponse> responses = plantMapper.toResponseList(plants);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete a plant",
        description = "Deletes a plant based on the provided ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Plant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Plant not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }
}
