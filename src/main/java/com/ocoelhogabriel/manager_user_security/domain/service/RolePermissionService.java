package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;

/**
 * Service interface for managing role permissions
 */
public interface RolePermissionService {
    
    /**
     * Find permission by role and resource
     * 
     * @param role The role
     * @param resource The resource
     * @return The permission for the role and resource
     */
    Permission findByRoleAndResource(Role role, Resource resource);
}