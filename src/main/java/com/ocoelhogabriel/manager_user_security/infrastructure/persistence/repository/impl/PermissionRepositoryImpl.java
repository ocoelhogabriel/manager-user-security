package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PermissionMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.RoleMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.RoleJpaRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the PermissionRepository interface. This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionJpaRepository permissionRepository;
    private final ResourceJpaRepository resourceRepository;
    private final RoleJpaRepository roleRepository;
    private final PermissionMapper permissionMapper;
    private final ResourceMapper resourceMapper;
    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param permissionRepository the JPA permission repository
     * @param resourceRepository   the JPA resource repository
     * @param roleRepository       the JPA role repository
     * @param permissionMapper     the permission mapper
     * @param resourceMapper       the resource mapper
     * @param roleMapper           the role mapper
     */
    public PermissionRepositoryImpl(PermissionJpaRepository permissionRepository, ResourceJpaRepository resourceRepository,
            RoleJpaRepository roleRepository, PermissionMapper permissionMapper, ResourceMapper resourceMapper, RoleMapper roleMapper) {
        this.permissionRepository = permissionRepository;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
        this.permissionMapper = permissionMapper;
        this.resourceMapper = resourceMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public Permission save(Permission permission) {
        var permissionEntity = permissionMapper.toEntity(permission);
        var savedEntity = permissionRepository.save(permissionEntity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id).map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Permission entity) {
        permissionRepository.delete(permissionMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return permissionRepository.existsById(id);
    }

    @Override
    public List<Permission> findByResource(String resourceName) {
        return permissionRepository.findByResourceName(resourceName).stream().map(permissionMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByResourceAndAction(String resourceName, String action) {
        return permissionRepository.findByResourceNameAndAction(resourceName, action).map(permissionMapper::toDomain);
    }

    @Override
    public boolean existsByResourceAndAction(String resource, String action) {
        return permissionRepository.existsByResourceNameAndAction(resource, action);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId).stream().map(permissionMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByRoleAndResource(Role role, Resource resource) {
        // Convert domain entities to JPA entities
        RoleEntity roleEntity = roleMapper.toEntity(role);
        ResourceEntity resourceEntity = resourceMapper.toEntity(resource);

        // Find by role and resource entities
        return permissionRepository.findByRoleAndResource(roleEntity, resourceEntity).map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findByRoleIdsAndResourceId(List<Long> roleIds, Long resourceId) {
        return permissionRepository.findByRoleIdInAndResourceId(roleIds, resourceId)
                .stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }
}
