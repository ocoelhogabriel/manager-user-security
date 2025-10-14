package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PermissionMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.ResourceMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.RoleMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionMapper permissionMapper;
    private final RoleMapper roleMapper;
    private final ResourceMapper resourceMapper;

    @Autowired
    public PermissionRepositoryAdapter(PermissionJpaRepository permissionJpaRepository, PermissionMapper permissionMapper, RoleMapper roleMapper, ResourceMapper resourceMapper) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.permissionMapper = permissionMapper;
        this.roleMapper = roleMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Permission save(Permission permission) {
        var entity = permissionMapper.toEntity(permission);
        var savedEntity = permissionJpaRepository.save(entity);
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
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Permission entity) {
        permissionJpaRepository.delete(permissionMapper.toEntity(entity));
    }

    @Override
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return permissionJpaRepository.findByRoleId(roleId).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByRoleIdsAndResourceId(List<Long> roleIds, Long resourceId) {
        return permissionJpaRepository.findByRoleIdInAndResourceId(roleIds, resourceId).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByRoleAndResource(Role role, Resource resource) {
        RoleEntity roleEntity = roleMapper.toEntity(role);
        ResourceEntity resourceEntity = resourceMapper.toEntity(resource);
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
                .collect(Collectors.toList());
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