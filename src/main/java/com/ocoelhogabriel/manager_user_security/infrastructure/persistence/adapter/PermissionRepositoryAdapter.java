package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.ResourceJpaRepository;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.PermissionMapper;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.RoleMapper;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.ResourceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final ResourceJpaRepository resourceJpaRepository;
    private final PermissionMapper permissionMapper;
    // TODO: These mappers below are pending refactoring
    private final RoleMapper roleMapper;
    private final ResourceMapper resourceMapper;

    public PermissionRepositoryAdapter(PermissionJpaRepository permissionJpaRepository, ResourceJpaRepository resourceJpaRepository, PermissionMapper permissionMapper, RoleMapper roleMapper, ResourceMapper resourceMapper) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.resourceJpaRepository = resourceJpaRepository;
        this.permissionMapper = permissionMapper;
        this.roleMapper = roleMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Permission save(Permission permission) {
        ResourceEntity resourceEntity = resourceJpaRepository.findByName(permission.getResource())
                .orElseThrow(() -> new DomainException("Cannot save permission: Resource not found: " + permission.getResource()));

        PermissionEntity permissionEntity = permissionMapper.toPersistenceEntity(permission);
        permissionEntity.setResource(resourceEntity);

        PermissionEntity savedEntity = permissionJpaRepository.save(permissionEntity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Permission permission) {
        if (permission != null && permission.getId() != null) {
            permissionJpaRepository.deleteById(permission.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return permissionJpaRepository.findByRoleId(roleId).stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Permission> findByRoleIdsAndResourceId(List<Long> roleIds, Long resourceId) {
        return permissionJpaRepository.findByRoleIdInAndResourceId(roleIds, resourceId).stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Permission> findByRoleAndResource(Role role, Resource resource) {
        RoleEntity roleEntity = roleMapper.toPersistenceEntity(role);
        ResourceEntity resourceEntity = resourceMapper.toPersistenceEntity(resource);
        return permissionJpaRepository.findByRoleAndResource(roleEntity, resourceEntity)
                .map(permissionMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return permissionJpaRepository.existsById(id);
    }

    @Override
    public List<Permission> findByResource(String resource) {
        return permissionJpaRepository.findByResourceName(resource).stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Permission> findByResourceAndAction(String resource, String action) {
        return permissionJpaRepository.findByResourceNameAndAction(resource, action)
                .map(permissionMapper::toDomain);
    }

    @Override
    public boolean existsByResourceAndAction(String resource, String action) {
        return permissionJpaRepository.existsByResourceNameAndAction(resource, action);
    }
}
