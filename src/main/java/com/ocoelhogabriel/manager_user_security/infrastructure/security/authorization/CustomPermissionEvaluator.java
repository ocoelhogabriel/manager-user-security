package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.valueobject.HttpMethod;

/**
 * Custom permission evaluator for method security
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionEvaluator permissionEvaluator;
    
    @Autowired
    private ResourceService resourceService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }
        
        String resourceName = targetDomainObject.toString();
        String permissionName = permission.toString();
        
        Resource resource = resourceService.findByName(resourceName);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
                
        if (resource == null || role == null) {
            return false;
        }
        
        // Convert permission to HTTP method
        HttpMethod method = switch (permissionName.toLowerCase()) {
            case "read" -> HttpMethod.GET;
            case "create" -> HttpMethod.POST;
            case "update" -> HttpMethod.PUT;
            case "delete" -> HttpMethod.DELETE;
            default -> null;
        };
        
        if (method == null) {
            return false;
        }
        
        UrlPathMatcher urlMatcher = new UrlPathMatcher(
                new com.ocoelhogabriel.manager_user_security.domain.valueobject.Resource(resource.getId(), resource.getName()), 
                "PERMISSION_CHECK");
        
        return permissionEvaluator.checkPermission(role, urlMatcher, method.name());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }
        
        Resource resource = resourceService.findByName(targetType);
        return hasPermission(authentication, resource, permission);
    }
}