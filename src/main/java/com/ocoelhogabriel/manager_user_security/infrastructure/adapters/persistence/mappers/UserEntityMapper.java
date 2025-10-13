package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.mappers;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;

/**
 * Mapper entre entidade de domínio e entidade JPA
 * Aplica Adapter Pattern - converte entre diferentes representações
 * Segue SOLID principles - Single Responsibility Principle
 */
@Component
public final class UserEntityMapper {
    
    /**
     * Converte de entidade de domínio para entidade JPA
     */
    public Usuario toEntity(final User user) {
        Objects.requireNonNull(user, "User cannot be null");
        
        final Usuario entity = Usuario.builder()
                .username(user.username().getValue())
                .email(user.email().getValue())
                .password(user.password().getValue())
                .ativo(user.isActive())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();

        // Se o usuário tem ID, setar na entidade
        if (user.hasId()) {
            entity.setId(user.id().getValue());
        }
        
        return entity;
    }
    
    /**
     * Converte de entidade JPA para entidade de domínio
     */
    public User toDomain(final Usuario entity) {
        Objects.requireNonNull(entity, "UserEntity cannot be null");
        
        final UserId id = Objects.nonNull(entity.getId()) ? UserId.of(entity.getId()) : null;
        final Username username = Username.of(entity.getUsername());
        final Email email = Email.of(entity.getEmail());
        final HashedPassword password = HashedPassword.of(entity.getPasswordHash());
        
        return User.restore(
                id,
                username,
                email,
                password,
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
