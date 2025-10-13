package com.ocoelhogabriel.manager_user_security.application.service.impl;

import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.service.RolePermissionService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;

/**
 * Implementation of the RolePermissionService.
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Override
    public Permission findByRoleAndResource(Role role, Resource resource) {
        if (role == null || resource == null) {
            throw new IllegalArgumentException("Role and resource cannot be null");
        }
        
        // This is a temporary implementation that always returns a Permission with all privileges
        // In a real implementation, you would look up the permission in a repository
        return new Permission(1L, "FULL_ACCESS", "Full access permission", resource);
    }
}