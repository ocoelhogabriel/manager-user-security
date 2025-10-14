package com.ocoelhogabriel.manager_user_security.application.usecase;

import com.ocoelhogabriel.manager_user_security.domain.service.RoleService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Use case for role management operations.
 * Acts as a facade over the role service to coordinate business processes.
 */
@Component
public class RoleUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RoleUseCase.class);
    
    private final RoleService roleService;
    
    @Autowired
    public RoleUseCase(RoleService roleService) {
        this.roleService = roleService;
    }
    
    /**
     * Get all roles in the system.
     *
     * @return List of all roles
     */
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        logger.debug("Getting all roles");
        return roleService.findAll();
    }
    
    /**
     * Get a role by its ID.
     *
     * @param id Role ID
     * @return The role with the specified ID
     * @throws ResourceNotFoundException if role not found
     */
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        logger.debug("Getting role with ID: {}", id);
        return roleService.findById(id);
    }
    
    /**
     * Get a role by its name.
     *
     * @param name Role name
     * @return The role with the specified name, or null if not found
     */
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        logger.debug("Getting role with name: {}", name);
        return roleService.findByName(name);
    }
    
    /**
     * Create a new role.
     *
     * @param name Role name
     * @param description Role description
     * @param active Whether the role is active
     * @param code Role code (optional)
     * @return The created role
     */
    @Transactional
    public Role createRole(String name, String description, boolean active, String code) {
        logger.info("Creating new role with name: {}", name);
        
        Role role = new Role(name, description, active, code);
        return roleService.create(role);
    }
    
    /**
     * Update an existing role.
     *
     * @param id Role ID
     * @param name Updated role name
     * @param description Updated role description
     * @param active Updated active status
     * @param code Updated role code
     * @return The updated role
     * @throws ResourceNotFoundException if role not found
     */
    @Transactional
    public Role updateRole(Long id, String name, String description, boolean active, String code) {
        logger.info("Updating role with ID: {}", id);
        
        // First fetch the existing role to preserve any data we're not updating
        Role existingRole = roleService.findById(id);
        
        // Update the fields
        existingRole.setName(name);
        existingRole.setDescription(description);
        existingRole.setActive(active);
        existingRole.setCode(code);
        
        return roleService.update(existingRole);
    }
    
    /**
     * Delete a role by its ID.
     *
     * @param id Role ID
     * @throws ResourceNotFoundException if role not found
     */
    @Transactional
    public void deleteRole(Long id) {
        logger.info("Deleting role with ID: {}", id);
        roleService.delete(id);
    }
    
    /**
     * Get all roles assigned to a specific user.
     *
     * @param userId User ID
     * @return Set of roles assigned to the user
     */
    @Transactional(readOnly = true)
    public Set<Role> getRolesByUserId(Long userId) {
        logger.debug("Getting roles for user with ID: {}", userId);
        return roleService.findByUserId(userId);
    }
    
    /**
     * Add a permission to a role.
     *
     * @param roleId Role ID
     * @param resourceId Resource ID
     * @param permissionName Permission name/action
     * @return The updated role
     * @throws ResourceNotFoundException if role or resource not found
     */
    @Transactional
    public Role addPermissionToRole(Long roleId, Long resourceId, String permissionName) {
        logger.info("Adding permission '{}' for resource ID {} to role ID {}", 
                permissionName, resourceId, roleId);
                
        return roleService.addPermission(roleId, resourceId, permissionName);
    }
    
    /**
     * Remove a permission from a role.
     *
     * @param roleId Role ID
     * @param permissionId Permission ID
     * @throws ResourceNotFoundException if role or permission not found
     */
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        logger.info("Removing permission ID {} from role ID {}", permissionId, roleId);
        roleService.removePermission(roleId, permissionId);
    }
    
    /**
     * Assign multiple permissions to a role by their IDs.
     *
     * @param roleId ID of the role to assign permissions to
     * @param resourceIds List of resource IDs to assign to the role
     * @return The updated role with the assigned permissions
     * @throws ResourceNotFoundException if role or any permission not found
     */
    @Transactional
    public Role assignPermissionsToRole(Long roleId, List<Long> resourceIds) {
        logger.info("Assigning {} resources to role ID {}", resourceIds.size(), roleId);
        return roleService.assignPermissions(roleId, resourceIds);
    }
    
    /**
     * Check if a role with the given name already exists.
     *
     * @param name Role name to check
     * @return true if a role with that name exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean roleNameExists(String name) {
        try {
            return roleService.findByName(name) != null;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }
}
