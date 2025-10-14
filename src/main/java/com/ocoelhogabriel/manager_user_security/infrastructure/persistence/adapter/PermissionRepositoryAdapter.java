package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.PermissionMapper;
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

    @Autowired
    public PermissionRepositoryAdapter(PermissionJpaRepository permissionJpaRepository, PermissionMapper permissionMapper) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.permissionMapper = permissionMapper;
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
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return permissionJpaRepository.findByRoles_Id(roleId).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByRoleIdsAndResourceId(List<Long> roleIds, Long resourceId) {
        return permissionJpaRepository.findByRoles_IdInAndResource_Id(roleIds, resourceId).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByRoleAndResource(Role role, Resource resource) {
        return permissionJpaRepository.findByRolesContainsAndResource(role, resource)
                .map(permissionMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return permissionJpaRepository.existsById(id);
    }

    @Override
    public List<Permission> findByResource(String resource) {
        return permissionJpaRepository.findByResource_Name(resource).stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Permission> findByResourceAndAction(String resource, String action) {
        // This assumes a custom method might be needed in JpaRepository if action is complex
        return permissionJpaRepository.findByResource_NameAndActionsContains(resource, action)
                .map(permissionMapper::toDomain);
    }

    @Override
    public boolean existsByResourceAndAction(String resource, String action) {
        return permissionJpaRepository.existsByResource_NameAndActionsContains(resource, action);
    }
}
