package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper for converting between User domain entity and UserEntity JPA entity.
 */
@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param roleMapper the role mapper
     */
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * Maps a JPA entity to a domain entity.
     *
     * @param entity the JPA entity
     * @return the domain entity
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.isActive()
        );

        // Map roles if they exist
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            entity.getRoles().forEach(roleEntity -> 
                user.addRole(roleMapper.toDomain(roleEntity))
            );
        }

        return user;
    }

    /**
     * Maps a domain entity to a JPA entity.
     *
     * @param domain the domain entity
     * @return the JPA entity
     */
    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPasswordHash());
        entity.setActive(domain.isActive());

        // Map roles if they exist
        if (domain.getRoles() != null && !domain.getRoles().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity> roles = domain.getRoles().stream()
                    .map(roleMapper::toEntity)
                    .collect(Collectors.toSet());
            
            entity.setRoles(roles);
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
    public UserEntity updateEntityFromDomain(UserEntity entity, User domain) {
        if (entity == null || domain == null) {
            return entity;
        }

        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        
        // Only update password if it's provided
        if (domain.getPasswordHash() != null && !domain.getPasswordHash().isEmpty()) {
            entity.setPassword(domain.getPasswordHash());
        }
        
        entity.setActive(domain.isActive());

        // Update roles if they exist in the domain object
        if (domain.getRoles() != null && !domain.getRoles().isEmpty()) {
            Set<com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity> roles = domain.getRoles().stream()
                    .map(roleMapper::toEntity)
                    .collect(Collectors.toSet());
            
            entity.setRoles(roles);
        }

        return entity;
    }
}
