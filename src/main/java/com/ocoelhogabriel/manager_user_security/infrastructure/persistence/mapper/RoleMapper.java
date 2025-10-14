package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;

/**
 * Mapper for converting between Role domain entity and RoleEntity JPA entity.
 */
@Component
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    /**
     * Constructor.
     *
     * @param permissionMapper the permission mapper
     */
    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    /**
     * Maps a JPA entity to a domain entity.
     *
     * @param entity the JPA entity
     * @return the domain entity
     */
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        // Create a new Role with the basic attributes
        Role role = new Role(entity.getName(), entity.getDescription());
        role.setId(entity.getId());
        role.setActive(entity.isActive());
        role.setCode(entity.getCode());
        role.setCreatedAt(entity.getCreatedAt());
        role.setUpdatedAt(entity.getUpdatedAt());

        // Map permissions if they exist
        if (entity.getPermissions() != null && !entity.getPermissions().isEmpty()) {
            Set<Permission> permissions = entity.getPermissions().stream()
                    .map(permissionMapper::toDomain)
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        return role;
    }

    /**
     * Maps a domain entity to a JPA entity.
     *
     * @param domain the domain entity
     * @return the JPA entity
     */
    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }

        RoleEntity entity = new RoleEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setActive(domain.isActive());
        entity.setCode(domain.getCode());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        // Map permissions if they exist
        if (domain.getPermissions() != null && !domain.getPermissions().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity> permissions =
                domain.getPermissions().stream()
                    .map(permissionMapper::toEntity)
                    .collect(Collectors.toSet());
            entity.setPermissions(permissions);
        }

        return entity;
    }

    /**
     * Updates a JPA entity with data from a domain entity.
     *
     * @param entity the JPA entity to update
     * @param domain the domain entity with updated data
     * @return the updated JPA entity
     */
    public RoleEntity updateEntityFromDomain(RoleEntity entity, Role domain) {
        if (entity == null || domain == null) {
            return entity;
        }

        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setActive(domain.isActive());
        entity.setCode(domain.getCode());
        entity.setUpdatedAt(domain.getUpdatedAt());

        // Update permissions if they exist in the domain object
        if (domain.getPermissions() != null && !domain.getPermissions().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity> permissions =
                domain.getPermissions().stream()
                    .map(permissionMapper::toEntity)
                    .collect(Collectors.toSet());
            entity.setPermissions(permissions);
        }

        return entity;
    }
}
