package com.ocoelhogabriel.usersecurity.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ocoelhogabriel.usersecurity.domain.entity.Permission;

/**
 * Repository interface for Permission entities.
 * Extends the generic Repository interface with Permission-specific methods.
 */
public interface PermissionRepository extends Repository<Permission, UUID> {
    
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
     * Checks if a permission for the given resource and action exists.
     *
     * @param resource the resource to check
     * @param action the action to check
     * @return true if a permission for the given resource and action exists, false otherwise
     */
    boolean existsByResourceAndAction(String resource, String action);
}