package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.List;
import java.util.Optional;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Repository interface for Permission entities.
 * Extends the generic Repository interface with Permission-specific methods.
 */
public interface PermissionRepository extends Repository<Permission, Long> {
    
    /**
     * Finds permissions by resource.
     *
     * @param resource the resource to search for
     * @return a list of permissions for the given resource
     */
    List<Permission> findByResource(String resource);
    
    /**
     * Finds a permission by resource and action.
     *
     * @param resource the resource to search for
     * @param action the action to search for
     * @return an Optional containing the found permission, or empty if not found
     */
    Optional<Permission> findByResourceAndAction(String resource, String action);
    
    /**
     * Finds a permission by role and resource.
     *
     * @param role the role to search for
     * @param resource the resource to search for
     * @return an Optional containing the found permission, or empty if not found
     */
    Optional<Permission> findByRoleAndResource(Role role, Resource resource);
    
    /**
     * Checks if a permission for the given resource and action exists.
     *
     * @param resource the resource to check
     * @param action the action to check
     * @return true if a permission for the given resource and action exists, false otherwise
     */
    boolean existsByResourceAndAction(String resource, String action);
}
