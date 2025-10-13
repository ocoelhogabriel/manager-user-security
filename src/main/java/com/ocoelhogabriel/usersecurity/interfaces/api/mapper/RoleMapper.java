package com.ocoelhogabriel.usersecurity.interfaces.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.application.service.PermissionService;
import com.ocoelhogabriel.usersecurity.domain.entity.Permission;
import com.ocoelhogabriel.usersecurity.domain.entity.Role;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.permission.PermissionResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.CreateRoleRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.RoleResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.role.UpdateRoleRequest;

/**
 * Mapper for Role entities and DTOs.
 */
@Component
public class RoleMapper {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    /**
     * Constructor.
     *
     * @param permissionService the permission service
     * @param permissionMapper the permission mapper
     */
    public RoleMapper(PermissionService permissionService, PermissionMapper permissionMapper) {
        this.permissionService = permissionService;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Maps a Role entity to RoleResponse DTO.
     *
     * @param role the Role entity
     * @return the RoleResponse DTO
     */
    public RoleResponse toResponse(Role role) {
        if (role == null) {
            return null;
        }

        RoleResponse response = new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
        
        // Load permissions for this role
        List<Permission> permissions = permissionService.findByRoleId(role.getId());
        if (permissions != null && !permissions.isEmpty()) {
            List<PermissionResponse> permissionResponses = permissions.stream()
                    .map(permissionMapper::toResponse)
                    .collect(Collectors.toList());
            response.setPermissions(permissionResponses);
        }
        
        return response;
    }

    /**
     * Maps a CreateRoleRequest DTO to Role entity.
     *
     * @param request the CreateRoleRequest DTO
     * @return the Role entity
     */
    public Role toEntity(CreateRoleRequest request) {
        if (request == null) {
            return null;
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return role;
    }

    /**
     * Maps an UpdateRoleRequest DTO to Role entity.
     *
     * @param request the UpdateRoleRequest DTO
     * @return the Role entity
     */
    public Role toEntity(UpdateRoleRequest request) {
        if (request == null) {
            return null;
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return role;
    }
}