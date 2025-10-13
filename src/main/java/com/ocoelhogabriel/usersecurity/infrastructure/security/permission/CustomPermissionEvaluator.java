package com.ocoelhogabriel.usersecurity.infrastructure.security.permission;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Custom permission evaluator for Spring Security.
 * This class is responsible for evaluating if a user has permission to access a resource.
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }
        
        String resource = targetDomainObject.toString();
        String action = permission.toString();
        
        return hasResourcePermission(authentication, resource, action);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }
        
        String resource = targetType;
        String action = permission.toString();
        
        return hasResourcePermission(authentication, resource, action);
    }
    
    /**
     * Checks if the authenticated user has permission to perform an action on a resource.
     * 
     * @param authentication the authentication
     * @param resource the resource
     * @param action the action
     * @return true if the user has permission, false otherwise
     */
    private boolean hasResourcePermission(Authentication authentication, String resource, String action) {
        // Check for admin role which has access to everything
        if (hasRole(authentication, "ADMIN")) {
            return true;
        }
        
        // Check for specific permission
        String requiredPermission = resource + ":" + action;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(requiredPermission));
    }
    
    /**
     * Checks if the authenticated user has a specific role.
     * 
     * @param authentication the authentication
     * @param role the role
     * @return true if the user has the role, false otherwise
     */
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}