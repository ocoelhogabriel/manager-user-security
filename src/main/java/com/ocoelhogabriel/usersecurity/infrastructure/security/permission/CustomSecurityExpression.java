package com.ocoelhogabriel.usersecurity.infrastructure.security.permission;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * Custom security expression class that extends Spring Security's expression system.
 * This class adds custom permission checks to be used in @PreAuthorize annotations.
 */
public class CustomSecurityExpression extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;
    
    /**
     * Constructor for CustomSecurityExpression.
     * 
     * @param authentication the authentication
     */
    public CustomSecurityExpression(Authentication authentication) {
        super(authentication);
    }
    
    /**
     * Checks if the current user has permission to perform the specified action on the resource.
     * 
     * @param resource the resource
     * @param action the action
     * @return true if the user has permission, false otherwise
     */
    public boolean hasResourcePermission(String resource, String action) {
        // Check for admin role which has access to everything
        if (hasRole("ADMIN")) {
            return true;
        }
        
        // Check for specific permission
        String requiredPermission = resource + ":" + action;
        return getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(requiredPermission));
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
    
    public void setThis(Object target) {
        this.target = target;
    }
}