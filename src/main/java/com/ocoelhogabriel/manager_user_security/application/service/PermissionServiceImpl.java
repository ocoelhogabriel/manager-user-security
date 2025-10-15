package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.PermissionService;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the PermissionService interface.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;
    private final ResourceService resourceService;
    private final UserService userService;

    public PermissionServiceImpl(PermissionRepository permissionRepository,
                                @Qualifier("resourceServiceImpl") ResourceService resourceService,
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
        Optional<User> userOptional = userService.findByIdWithRoles(userId);
        if (userOptional.isEmpty()) {
            logger.warn("User with ID {} not found when checking permissions", userId);
            return false;
        }
        User user = userOptional.get();

        List<Resource> resources = resourceService.findMatchingResources(path, method);
        if (resources.isEmpty()) {
            logger.debug("No resources found for path {} and method {}", path, method);
            return false;
        }

        List<Long> resourceIds = resources.stream().map(Resource::getId).toList();
        List<Long> roleIds = user.getRoles().stream().map(Role::getId).toList();

        // Corrected Logic: Check if a permission exists for any of the user's roles against any of the matching resources.
        boolean hasPermission = resourceIds.stream()
                .anyMatch(resourceId -> !permissionRepository.findByRoleIdsAndResourceId(roleIds, resourceId).isEmpty());

        if (hasPermission) {
            logger.debug("User {} has permission to access {} {}", userId, method, path);
        } else {
            logger.warn("User {} does not have permission to access {} {}", userId, method, path);
        }

        return hasPermission;
    }
}
