package com.ocoelhogabriel.manager_user_security.interfaces.mapper;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.RoleService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.CreatePermissionRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.PermissionResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.UpdatePermissionRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
     * @param roleService     the role service
     * @param resourceMapper  the resource mapper
     */
    public PermissionMapper(ResourceService resourceService, RoleService roleService, ResourceMapper resourceMapper) {
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
        // In the new structure, the name is the resource itself
        response.setName(permission.getResource());
        // Description doesn't exist in the new entity, temporarily use resource as description
        response.setDescription("Permission for " + permission.getResource());

        // Resource is now a String in the new entity, not an object
        if (permission.getResource() != null) {
            // We can create a temporary Resource object for compatibility
            Resource tempResource = resourceService.findByName(permission.getResource());
            if (tempResource != null) {
                response.setResource(resourceMapper.toResponse(tempResource));
            }
        }

        // Role is no longer directly in the Permission entity
        // This part will need to be adapted in a more complete implementation

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

        // Create actions set with the single action from request
        Set<String> actions = new HashSet<>();
        actions.add(request.getAction());

        // Create a new Permission using constructor with values from request
        Permission permission = new Permission(null,  // ID will be assigned by the database
                request.getResourceName(), actions);

        return permission;
    }

    /**
     * Maps an UpdatePermissionRequest DTO to Permission entity.
     *
     * @param id      the ID of the permission
     * @param request the UpdatePermissionRequest DTO
     * @return the Permission entity
     */
    public Permission toEntity(Long id, UpdatePermissionRequest request) {
        if (request == null) {
            return null;
        }

        // Extract action and resource from request
        String action = request.getAction();
        String resourceName = request.getResourceName();

        // Create actions set with the action from request
        Set<String> actions = new HashSet<>();
        if (action != null && !action.isEmpty()) {
            actions.add(action);
        }

        // Create a Permission with the existing ID and updated values
        Permission permission = new Permission(id, resourceName, actions);

        return permission;
    }

    public Permission toEntityWithId(@Valid UpdatePermissionRequest request, Long id) {
        return toEntity(id, request);
    }
}
