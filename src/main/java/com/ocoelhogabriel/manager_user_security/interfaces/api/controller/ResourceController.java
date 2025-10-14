package com.ocoelhogabriel.manager_user_security.interfaces.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.resource.CreateResourceRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.resource.ResourceResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.resource.UpdateResourceRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.mapper.ResourceMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing resources.
 */
@RestController
@RequestMapping("/api/resources/v1")
@Tag(name = "Resources", description = "API for managing resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;

    public ResourceController(ResourceService resourceService, ResourceMapper resourceMapper) {
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
    }

    /**
     * Get all resources.
     *
     * @return list of resources
     */
    @GetMapping
    @Operation(summary = "Get all resources", description = "Retrieves a list of all resources")
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        List<Resource> resources = resourceService.findAll();
        List<ResourceResponse> response = resources.stream()
                .map(resourceMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get a resource by ID.
     *
     * @param id the resource ID
     * @return the resource
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID", description = "Retrieves a resource by its ID")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Long id) {
        Resource resource = resourceService.findById(id);
        return ResponseEntity.ok(resourceMapper.toResponse(resource));
    }

    /**
     * Create a new resource.
     *
     * @param request the resource creation request
     * @return the created resource
     */
    @PostMapping
    @Operation(summary = "Create resource", description = "Creates a new resource")
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody CreateResourceRequest request) {
        Resource resource = resourceMapper.toEntity(request);
        Resource createdResource = resourceService.create(resource);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resourceMapper.toResponse(createdResource));
    }

    /**
     * Update an existing resource.
     *
     * @param id the resource ID
     * @param request the resource update request
     * @return the updated resource
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update resource", description = "Updates an existing resource")
    public ResponseEntity<ResourceResponse> updateResource(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResourceRequest request) {
        // Primeiro busca o recurso existente para obter o UUID correspondente
        Resource existingResource = resourceService.findById(id);
        
        // Usa o método updateEntity do mapper que aceita o recurso existente
        Resource updatedResource = resourceMapper.updateEntity(request, existingResource);
        
        // Atualiza o recurso e retorna a resposta
        Resource savedResource = resourceService.update(updatedResource);
        return ResponseEntity.ok(resourceMapper.toResponse(savedResource));
    }

    /**
     * Delete a resource.
     *
     * @param id the resource ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resource", description = "Deletes a resource by its ID")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
