package com.ocoelhogabriel.manager_user_security.infrastructure.security.mapper;

import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain Permission entity and security Permission value object.
 */
@Component
public class SecurityPermissionMapper {

    /**
     * Maps a domain permission to a security permission.
     *
     * @param domainPermission the domain permission entity
     * @param domainResource the domain resource entity
     * @return the security permission value object
     */
    public com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission toSecurityPermission(
            com.ocoelhogabriel.manager_user_security.domain.entity.Permission domainPermission, 
            com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource) {
        if (domainPermission == null || domainResource == null) {
            return null;
        }
        
        // Create security resource
        com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource securityResource = 
            new com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource(
                domainResource.getName(),
                domainResource.getDescription()
        );
        
        // Check if specific actions are present in the domain permission
        boolean canCreate = domainPermission.getActions().contains("CREATE");
        boolean canRead = domainPermission.getActions().contains("READ");
        boolean canList = domainPermission.getActions().contains("LIST");
        boolean canEdit = domainPermission.getActions().contains("EDIT") || domainPermission.getActions().contains("UPDATE");
        boolean canDelete = domainPermission.getActions().contains("DELETE");
        
        // Create security permission with all action flags
        return new com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission(
                domainPermission.getId(),
                domainResource.getName() + "_PERMISSION",
                "Permission for " + domainResource.getName(),
                securityResource,
                canCreate,
                canRead,
                canList,
                canEdit,
                canDelete
        );
    }
}