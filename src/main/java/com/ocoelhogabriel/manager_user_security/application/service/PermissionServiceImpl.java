package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.service.PermissionService;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the PermissionService interface.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;
    private final ResourceService resourceService;
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param permissionRepository the permission repository
     * @param resourceService      the resource service
     * @param userService          the user service
     */
    public PermissionServiceImpl(PermissionRepository permissionRepository, ResourceService resourceService,
            UserService userService) {
        this.permissionRepository = permissionRepository;
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Permission findById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission", id));
    }

    @Override
    @Transactional
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission update(Permission permission) {
        // Check if permission exists
        if (!permissionRepository.existsById(permission.getId())) {
            throw new ResourceNotFoundException("Permission", permission.getId());
        }

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission", id);
        }

        permissionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(Long userId, String path, String method) {
        // Get user with roles
        User user = userService.findByIdWithRoles(userId);
        if (user == null) {
            logger.warn("User with ID {} not found when checking permissions", userId);
            return false;
        }

        // Get resources that match the path and method
        List<Resource> resources = resourceService.findByPathAndMethod(path, method);
        if (resources.isEmpty()) {
            logger.debug("No resources found for path {} and method {}", path, method);
            return false;
        }

        // Extract resource IDs
        List<Long> resourceIds = resources.stream().map(Resource::getId).collect(Collectors.toList());

        // Extract role IDs
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());

        // Check if any permission exists for any of the user's roles and any matching resource
        boolean hasPermission = permissionRepository.findByRoleIdsAndResourceId(roleIds, resourceIds.get(0)).size() > 0;
        
        if (hasPermission) {
            logger.debug("User {} has permission to access {} {}", userId, method, path);
        } else {
            logger.warn("User {} does not have permission to access {} {}", userId, method, path);
        }

        return hasPermission;
    }
}
