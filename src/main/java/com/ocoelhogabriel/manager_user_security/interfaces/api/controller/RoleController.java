package com.ocoelhogabriel.manager_user_security.interfaces.api.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.application.usecase.RoleUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.role.AddPermissionRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.role.CreateRoleRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.role.RoleResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.role.UpdateRoleRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.mapper.RoleMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing roles.
 */
@RestController
@RequestMapping("/api/roles/v1")
@Tag(name = "Roles", description = "API for managing roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleUseCase roleUseCase;
    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param roleUseCase the role use case
     * @param roleMapper the role mapper
     */
    public RoleController(RoleUseCase roleUseCase, RoleMapper roleMapper) {
        this.roleUseCase = roleUseCase;
        this.roleMapper = roleMapper;
    }

    /**
     * Get all roles.
     *
     * @return list of roles
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
        summary = "Get all roles", 
        description = "Retrieves a list of all roles",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        }
    )
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<Role> roles = roleUseCase.getAllRoles();
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
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(
        summary = "Get role by ID", 
        description = "Retrieves a role by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        }
    )
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        Role role = roleUseCase.getRoleById(id);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    /**
     * Create a new role.
     *
     * @param request the role creation request
     * @return the created role
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create role", 
        description = "Creates a new role",
        responses = {
            @ApiResponse(responseCode = "201", description = "Role created successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Role with same name already exists")
        }
    )
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role createdRole = roleUseCase.createRole(
                request.getName(),
                request.getDescription(),
                request.isActive(),
                request.getCode()
        );
        
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update role", 
        description = "Updates an existing role",
        responses = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Role name would conflict with existing role")
        }
    )
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {
        
        Role updatedRole = roleUseCase.updateRole(
                id,
                request.getName(),
                request.getDescription(),
                request.isActive(),
                request.getCode()
        );
        
        return ResponseEntity.ok(roleMapper.toResponse(updatedRole));
    }

    /**
     * Delete a role.
     *
     * @param id the role ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete role", 
        description = "Deletes a role by its ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Cannot delete role in use by users")
        }
    )
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleUseCase.deleteRole(id);
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Add permission to role", 
        description = "Adds a resource permission to a role",
        responses = {
            @ApiResponse(responseCode = "200", description = "Permission added successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Role or resource not found")
        }
    )
    public ResponseEntity<RoleResponse> addPermission(
            @PathVariable Long id,
            @Valid @RequestBody AddPermissionRequest request) {
        Role updatedRole = roleUseCase.addPermissionToRole(id, request.getResourceId(), request.getName());
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Remove permission from role", 
        description = "Removes a permission from a role",
        responses = {
            @ApiResponse(responseCode = "204", description = "Permission removed successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Role or permission not found")
        }
    )
    public ResponseEntity<Void> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        roleUseCase.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get roles by user ID.
     *
     * @param userId the user ID
     * @return list of roles
     */
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @securityService.hasUserAccess(#userId)")
    @Operation(
        summary = "Get roles by user", 
        description = "Retrieves a list of roles for a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    public ResponseEntity<List<RoleResponse>> getRolesByUserId(@PathVariable Long userId) {
        Set<Role> roleSet = roleUseCase.getRolesByUserId(userId);
        List<RoleResponse> response = roleSet.stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
