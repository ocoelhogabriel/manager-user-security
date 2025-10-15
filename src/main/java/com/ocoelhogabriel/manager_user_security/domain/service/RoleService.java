package com.ocoelhogabriel.manager_user_security.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Service interface for managing roles.
 */
public interface RoleService {

    /**
     * Find all roles.
     *
     * @return a list of roles
     */
    List<Role> findAll();

    /**
     * Find a role by ID.
     *
     * @param id the role ID
     * @return the role
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if role not found
     */
    Role findById(Long id);

    /**
     * Find a role by name.
     *
     * @param name the role name
     * @return an Optional containing the role, or empty if not found
     */
    Optional<Role> findByName(String name);

    /**
     * Create a new role.
     *
     * @param role the role to create
     * @return the created role
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.DuplicateResourceException if role with same name already exists
     */
    Role create(Role role);

    /**
     * Update an existing role.
     *
     * @param role the role to update
     * @return the updated role
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if role not found
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.DuplicateResourceException if role name would conflict with existing role
     */
    Role update(Role role);

    /**
     * Delete a role.
     *
     * @param id the ID of the role to delete
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if role not found
     */
    void delete(Long id);
    
    /**
     * Find roles by user ID.
     *
     * @param userId the user ID
     * @return the roles
     */
    Set<Role> findByUserId(Long userId);
    
    /**
     * Add resource permission to role.
     *
     * @param roleId the role ID
     * @param resourceId the resource ID
     * @param permissionName the permission name
     * @return the updated role
     */
    Role addPermission(Long roleId, Long resourceId, String permissionName);
    
    /**
     * Remove resource permission from role.
     *
     * @param roleId the role ID
     * @param permissionId the permission ID
     */
    void removePermission(Long roleId, Long permissionId);

    /**
     * Find all active roles.
     *
     * @return a set of active roles
     */
    Set<Role> findByActive();

    /**
     * Assign permissions to a role.
     *
     * @param roleId the ID of the role to assign permissions to
     * @param permissionIds the list of permission IDs to assign
     * @return the updated role with assigned permissions
     * @throws com.ocoelhogabriel.usersecurity.domain.exception.ResourceNotFoundException if role not found
     */
    Role assignPermissions(Long roleId, List<Long> permissionIds);
}
