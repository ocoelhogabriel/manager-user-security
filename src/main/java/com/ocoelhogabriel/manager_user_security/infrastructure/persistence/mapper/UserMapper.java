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

        // Create a base user with username, email and password
        User user = new User(entity.getUsername(), entity.getEmail(), entity.getPassword());

        // Set additional properties
        user.setId(entity.getId());
        user.setActive(entity.isActive());
        user.setFullName(entity.getFullName());
        user.setImage(entity.getImage());
        user.setRegistrationNumber(entity.getRegistrationNumber());
        user.setPhone(entity.getPhone());
        user.setCpf(entity.getCpf());

        // Company related properties 
        user.setCompanyId(entity.getCompanyId());
        user.setCompanyName(entity.getCompanyName());

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
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPasswordHash());
        entity.setFullName(domain.getFullName());
        entity.setActive(domain.isActive());
        entity.setImage(domain.getImage());
        entity.setRegistrationNumber(domain.getRegistrationNumber());
        entity.setPhone(domain.getPhone());
        entity.setCpf(domain.getCpf());
        entity.setCompanyId(domain.getCompanyId());
        entity.setCompanyName(domain.getCompanyName());

        // Map roles if they exist
        if (domain.getRoles() != null && !domain.getRoles().isEmpty()) {
            domain.getRoles().forEach(role -> {
                if (role != null) {
                    entity.addRole(roleMapper.toEntity(role));
                }
            });
        }

        return entity;
    }
}
