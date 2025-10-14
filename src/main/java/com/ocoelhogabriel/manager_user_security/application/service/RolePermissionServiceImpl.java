package com.ocoelhogabriel.manager_user_security.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.ResourceRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.RolePermissionService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.mapper.SecurityPermissionMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the RolePermissionService.
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final SecurityPermissionMapper securityPermissionMapper;

    @Autowired
    public RolePermissionServiceImpl(PermissionRepository permissionRepository, ResourceRepository resourceRepository,
            SecurityPermissionMapper securityPermissionMapper) {
        this.permissionRepository = permissionRepository;
        this.resourceRepository = resourceRepository;
        this.securityPermissionMapper = securityPermissionMapper;
    }

    @Override
    public com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission findByRoleAndResource(Role role,
            com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource securityResource) {
        if (role == null || securityResource == null) {
            throw new IllegalArgumentException("Role and resource cannot be null");
        }

        // Find the domain resource by name
        Optional<com.ocoelhogabriel.manager_user_security.domain.entity.Resource> domainResourceOpt = resourceRepository.findByName(securityResource.name());

        if (domainResourceOpt.isEmpty()) {
            // If resource not found, return a default permission with no privileges
            return new com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission(null,
                    "NO_ACCESS",
                    "No access permission",
                    securityResource,
                    false,
                    false,
                    false,
                    false,
                    false);
        }

        com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource = domainResourceOpt.get();

        // Find permission for the role and resource
        Optional<com.ocoelhogabriel.manager_user_security.domain.entity.Permission> domainPermissionOpt = permissionRepository.findByRoleAndResource(
                role,
                domainResource);

        if (domainPermissionOpt.isEmpty()) {
            // If permission not found, return a default permission with no privileges
            return new com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission(null,
                    "NO_ACCESS",
                    "No access permission",
                    securityResource,
                    false,
                    false,
                    false,
                    false,
                    false);
        }

        // Convert domain permission to security permission
        return securityPermissionMapper.toSecurityPermission(domainPermissionOpt.get(), domainResource);
    }
}