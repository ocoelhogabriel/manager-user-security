package com.ocoelhogabriel.manager_user_security.domain.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;

/**
 * Service interface for authorization operations.
 * This interface defines the contract for checking user permissions.
 */
public interface AuthorizationService {
    
    /**
     * Checks if a user has permission to perform an action on a resource.
     *
     * @param user the user to check permissions for
     * @param resource the resource name
     * @param action the action to check
     * @return true if the user has permission, false otherwise
     */
    boolean hasPermission(User user, String resource, String action);
    
    /**
     * Validates if a user can access a specific URL with the given HTTP method.
     *
     * @param user the user to validate access for
     * @param url the URL to check
     * @param method the HTTP method to check
     * @return true if the user can access the URL with the given method, false otherwise
     */
    boolean canAccessUrl(User user, String url, String method);
    
    /**
     * Checks if a user has a specific role.
     *
     * @param user the user to check roles for
     * @param roleName the name of the role to check
     * @return true if the user has the role, false otherwise
     */
    boolean hasRole(User user, String roleName);
}
