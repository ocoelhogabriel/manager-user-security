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

import com.ocoelhogabriel.usersecurity.application.service.RoleService;
import com.ocoelhogabriel.usersecurity.domain.entity.Role;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.AddPermissionRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.CreateRoleRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.RoleResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.UpdateRoleRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.mapper.RoleMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing roles.
 */
@RestController
@RequestMapping("/api/roles/v1")
@Tag(name = "Roles", description = "API for managing roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param roleService the role service
     * @param roleMapper the role mapper
     */
    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    /**
     * Get all roles.
     *
     * @return list of roles
     */
    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieves a list of all roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        List<RoleResponse> response = roles.stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Get a role by ID.
     *
     * @param id the role ID
     * @return the role
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieves a role by its ID")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    /**
     * Create a new role.
     *
     * @param request the role creation request
     * @return the created role
     */
    @PostMapping
    @Operation(summary = "Create role", description = "Creates a new role")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role role = roleMapper.toEntity(request);
        Role createdRole = roleService.create(role);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleMapper.toResponse(createdRole));
    }

    /**
     * Update an existing role.
     *
     * @param id the role ID
     * @param request the role update request
     * @return the updated role
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update role", description = "Updates an existing role")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {
        Role role = roleMapper.toEntity(request);
        role.setId(id);
        Role updatedRole = roleService.update(role);
        return ResponseEntity.ok(roleMapper.toResponse(updatedRole));
    }

    /**
     * Delete a role.
     *
     * @param id the role ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role", description = "Deletes a role by its ID")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Add a permission to a role.
     *
     * @param id the role ID
     * @param request the permission request
     * @return the updated role
     */
    @PostMapping("/{id}/permissions")
    @Operation(summary = "Add permission to role", description = "Adds a resource permission to a role")
    public ResponseEntity<RoleResponse> addPermission(
            @PathVariable Long id,
            @Valid @RequestBody AddPermissionRequest request) {
        Role updatedRole = roleService.addPermission(id, request.getResourceId(), request.getName());
        return ResponseEntity.ok(roleMapper.toResponse(updatedRole));
    }
    
    /**
     * Remove a permission from a role.
     *
     * @param roleId the role ID
     * @param permissionId the permission ID
     * @return no content
     */
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Remove permission from role", description = "Removes a permission from a role")
    public ResponseEntity<Void> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        roleService.removePermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get roles by user ID.
     *
     * @param userId the user ID
     * @return list of roles
     */
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get roles by user", description = "Retrieves a list of roles for a specific user")
    public ResponseEntity<List<RoleResponse>> getRolesByUserId(@PathVariable Long userId) {
        List<Role> roles = roleService.findByUserId(userId).stream().toList();
        List<RoleResponse> response = roles.stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}