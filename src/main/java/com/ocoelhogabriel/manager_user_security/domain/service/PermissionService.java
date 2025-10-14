package com.ocoelhogabriel.manager_user_security.domain.service;

import java.util.List;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;

/**
 * Service interface for managing permissions.
 */
public interface PermissionService {

    /**
     * Find all permissions.
     *
     * @return a list of permissions
     */
    List<Permission> findAll();

    /**
     * Find a permission by ID.
     *
     * @param id the permission ID
     * @return the permission
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if permission not found
     */
    Permission findById(Long id);

    /**
     * Create a new permission.
     *
     * @param permission the permission to create
     * @return the created permission
     */
    Permission create(Permission permission);

    /**
     * Update an existing permission.
     *
     * @param permission the permission to update
     * @return the updated permission
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if permission not found
     */
    Permission update(Permission permission);

    /**
     * Delete a permission.
     *
     * @param id the ID of the permission to delete
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if permission not found
     */
    void delete(Long id);

    /**
     * Find permissions by role ID.
     *
     * @param roleId the role ID
     * @return the permissions
     */
    List<Permission> findByRoleId(Long roleId);
    
    /**
     * Check if user has permission to access a resource.
     *
     * @param userId the user ID
     * @param path the resource path
     * @param method the HTTP method
     * @return true if permitted, false otherwise
     */
    boolean hasPermission(Long userId, String path, String method);
}
