package com.ocoelhogabriel.manager_user_security.application.service.impl;

import com.ocoelhogabriel.manager_user_security.application.service.PermissionService;
import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PermissionMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
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

    private final PermissionJpaRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final ResourceService resourceService;
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param permissionRepository the permission repository
     * @param permissionMapper     the permission mapper
     * @param resourceService      the resource service
     * @param userService          the user service
     */
    public PermissionServiceImpl(PermissionJpaRepository permissionRepository, PermissionMapper permissionMapper, ResourceService resourceService,
            UserService userService) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Permission findById(Long id) {
        return permissionRepository.findById(id).map(permissionMapper::toDomain).orElseThrow(() -> new ResourceNotFoundException("Permission", id));
    }

    @Override
    @Transactional
    public Permission create(Permission permission) {
        PermissionEntity entity = permissionMapper.toEntity(permission);
        entity = permissionRepository.save(entity);
        return permissionMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public Permission update(Permission permission) {
        // Check if permission exists
        if (!permissionRepository.existsById(permission.getId())) {
            throw new ResourceNotFoundException("Permission", permission.getId());
        }

        PermissionEntity entity = permissionMapper.toEntity(permission);
        entity = permissionRepository.save(entity);
        return permissionMapper.toDomain(entity);
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
        return permissionRepository.findByRoleId(roleId).stream().map(permissionMapper::toDomain).collect(Collectors.toList());
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
        Optional<PermissionEntity> permission = permissionRepository.findByRoleIdInAndResourceIdIn(roleIds, resourceIds);

        boolean hasPermission = permission.isPresent();
        if (hasPermission) {
            logger.debug("User {} has permission to access {} {}", userId, method, path);
        } else {
            logger.warn("User {} does not have permission to access {} {}", userId, method, path);
        }

        return hasPermission;
    }
}
