package com.ocoelhogabriel.manager_user_security.interfaces.api.mapper;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.application.service.RoleService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.permission.CreatePermissionRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.permission.PermissionResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.permission.UpdatePermissionRequest;

/**
 * Mapper for Permission entities and DTOs.
 */
@Component
public class PermissionMapper {

    private final ResourceService resourceService;
    private final RoleService roleService;
    private final ResourceMapper resourceMapper;

    /**
     * Constructor.
     *
     * @param resourceService the resource service
     * @param roleService the role service
     * @param resourceMapper the resource mapper
     */
    public PermissionMapper(
            ResourceService resourceService, 
            RoleService roleService,
            ResourceMapper resourceMapper) {
        this.resourceService = resourceService;
        this.roleService = roleService;
        this.resourceMapper = resourceMapper;
    }

    /**
     * Maps a Permission entity to PermissionResponse DTO.
     *
     * @param permission the Permission entity
     * @return the PermissionResponse DTO
     */
    public PermissionResponse toResponse(Permission permission) {
        if (permission == null) {
            return null;
        }

        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        
        if (permission.getResource() != null) {
            response.setResource(resourceMapper.toResponse(permission.getResource()));
        }
        
        if (permission.getRole() != null) {
            response.setRoleId(permission.getRole().getId());
            response.setRoleName(permission.getRole().getName());
        }
        
        return response;
    }

    /**
     * Maps a CreatePermissionRequest DTO to Permission entity.
     *
     * @param request the CreatePermissionRequest DTO
     * @return the Permission entity
     */
    public Permission toEntity(CreatePermissionRequest request) {
        if (request == null) {
            return null;
        }

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        
        if (request.getResourceId() != null) {
            Resource resource = resourceService.findById(request.getResourceId());
            permission.setResource(resource);
        }
        
        if (request.getRoleId() != null) {
            Role role = roleService.findById(request.getRoleId());
            permission.setRole(role);
        }
        
        return permission;
    }

    /**
     * Maps an UpdatePermissionRequest DTO to Permission entity.
     *
     * @param request the UpdatePermissionRequest DTO
     * @return the Permission entity
     */
    public Permission toEntity(UpdatePermissionRequest request) {
        if (request == null) {
            return null;
        }

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        
        if (request.getResourceId() != null) {
            Resource resource = resourceService.findById(request.getResourceId());
            permission.setResource(resource);
        }
        
        if (request.getRoleId() != null) {
            Role role = roleService.findById(request.getRoleId());
            permission.setRole(role);
        }
        
        return permission;
    }
}
