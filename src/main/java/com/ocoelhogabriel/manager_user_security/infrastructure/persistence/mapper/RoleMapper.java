package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

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

        Role role = new Role(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );

        // Map permissions if they exist
        if (entity.getPermissions() != null && !entity.getPermissions().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.domain.entity.Permission> permissions = entity.getPermissions().stream()
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

        // Map permissions if they exist
        if (domain.getPermissions() != null && !domain.getPermissions().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity> permissions = new HashSet<>();
            
            domain.getPermissions().forEach(permission -> {
                com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity permissionEntity = permissionMapper.toEntity(permission);
                permissions.add(permissionEntity);
                permissionEntity.setRole(entity);
            });
            
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

        return entity;
    }
}
