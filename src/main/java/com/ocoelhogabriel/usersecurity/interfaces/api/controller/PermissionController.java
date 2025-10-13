package com.ocoelhogabriel.usersecurity.interfaces.api.controller;

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

import com.ocoelhogabriel.usersecurity.application.service.PermissionService;
import com.ocoelhogabriel.usersecurity.domain.entity.Permission;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.permission.CreatePermissionRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.permission.PermissionResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.permission.UpdatePermissionRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.mapper.PermissionMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing permissions.
 */
@RestController
@RequestMapping("/api/permissions/v1")
@Tag(name = "Permissions", description = "API for managing permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    /**
     * Constructor.
     *
     * @param permissionService the permission service
     * @param permissionMapper the permission mapper
     */
    public PermissionController(PermissionService permissionService, PermissionMapper permissionMapper) {
        this.permissionService = permissionService;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Get all permissions.
     *
     * @return list of permissions
     */
    @GetMapping
    @Operation(summary = "Get all permissions", description = "Retrieves a list of all permissions")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<Permission> permissions = permissionService.findAll();
        List<PermissionResponse> response = permissions.stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get a permission by ID.
     *
     * @param id the permission ID
     * @return the permission
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get permission by ID", description = "Retrieves a permission by its ID")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.findById(id);
        return ResponseEntity.ok(permissionMapper.toResponse(permission));
    }

    /**
     * Create a new permission.
     *
     * @param request the permission creation request
     * @return the created permission
     */
    @PostMapping
    @Operation(summary = "Create permission", description = "Creates a new permission")
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = permissionMapper.toEntity(request);
        Permission createdPermission = permissionService.create(permission);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permissionMapper.toResponse(createdPermission));
    }

    /**
     * Update an existing permission.
     *
     * @param id the permission ID
     * @param request the permission update request
     * @return the updated permission
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update permission", description = "Updates an existing permission")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePermissionRequest request) {
        Permission permission = permissionMapper.toEntity(request);
        permission.setId(id);
        Permission updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(permissionMapper.toResponse(updatedPermission));
    }

    /**
     * Delete a permission.
     *
     * @param id the permission ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete permission", description = "Deletes a permission by its ID")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get permissions by role ID.
     *
     * @param roleId the role ID
     * @return list of permissions
     */
    @GetMapping("/by-role/{roleId}")
    @Operation(summary = "Get permissions by role", description = "Retrieves a list of permissions for a specific role")
    public ResponseEntity<List<PermissionResponse>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Permission> permissions = permissionService.findByRoleId(roleId);
        List<PermissionResponse> response = permissions.stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}