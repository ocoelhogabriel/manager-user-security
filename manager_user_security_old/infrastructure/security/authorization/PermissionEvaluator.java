package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.RolePermissionService;

/**
 * Component for evaluating user permissions
 */
@Component
public class PermissionEvaluator {

    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private RolePermissionService rolePermissionService;
    
    /**
     * Checks if a role has permission to access a resource with a specific method
     *
     * @param roleName   The role name
     * @param urlMatcher The URL matcher containing the resource
     * @param method     The HTTP method
     * @return True if the role has permission, false otherwise
     */
    public boolean checkPermission(String roleName, UrlPathMatcher urlMatcher, String method) {
        // Extract resource name
        Resource resource = urlMatcher.getResource();
        String resourceName = resource.name();
        
        // Validate parameters
        Objects.requireNonNull(resourceName, "Resource name cannot be null");
        Objects.requireNonNull(roleName, "Role name cannot be null");
        Objects.requireNonNull(urlMatcher, "URL matcher cannot be null");

        // Get resource and role entities
        var resourceEntity = resourceService.findByName(resourceName);
        var roleEntity = new Role();
        roleEntity.setName(roleName);
        
        // Get permission for the role and resource
        var permission = rolePermissionService.findByRoleAndResource(roleEntity, resourceEntity);

        // Check permission based on HTTP method
        return switch (method.toUpperCase()) {
            case "GET" -> {
                if (urlMatcher.getMessage().equalsIgnoreCase("SEARCH")) {
                    yield permission.canRead();
                } else {
                    yield permission.canList();
                }
            }
            case "POST" -> permission.canCreate();
            case "PUT" -> permission.canEdit();
            case "DELETE" -> permission.canDelete();
            default -> false;
        };
    }
}