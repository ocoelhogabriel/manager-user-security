package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

import org.springframework.stereotype.Component;

/**
 * Custom security expression for use in SpEL expressions
 */
@Component("securityExpression")
public class CustomSecurityExpression {
    
    /**
     * Check if the current user has the required role
     * 
     * @param role The role to check
     * @return True if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        // This will be implemented with integration to Spring Security context
        return true;
    }
    
    /**
     * Check if the current user has permission for a resource
     * 
     * @param resource The resource name
     * @param permission The permission name
     * @return True if the user has permission, false otherwise
     */
    public boolean hasPermission(String resource, String permission) {
        // This will be implemented with integration to CustomPermissionEvaluator
        return true;
    }
}