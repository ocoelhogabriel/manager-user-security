package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence;

import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.jpa.UserJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.mappers.UserEntityMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementação JPA do repositório de usuários
 * Aplica Adapter Pattern - adapta JPA para interface do domínio
 * Segue SOLID principles - Dependency Inversion Principle
 */
@Repository
public class JpaUserRepositoryAdapter implements UserRepository {
    
    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;
    
    public JpaUserRepositoryAdapter(final UserJpaRepository jpaRepository,
                                   final UserEntityMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository, "UserJpaRepository cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "UserEntityMapper cannot be null");
    }
    
    @Override
    public User save(final User user) {
        Objects.requireNonNull(user, "User cannot be null");
        
        final Usuario entity = this.mapper.toEntity(user);
        final Usuario savedEntity = this.jpaRepository.save(entity);
        return this.mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<User> findById(final UserId id) {
        Objects.requireNonNull(id, "UserId cannot be null");
        
        return this.jpaRepository.findById(id.getValue())
                .map(this.mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(final Username username) {
        Objects.requireNonNull(username, "Username cannot be null");
        
        return this.jpaRepository.findByUsername(username.getValue())
                .map(this.mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(final Email email) {
        Objects.requireNonNull(email, "Email cannot be null");
        
        return this.jpaRepository.findByEmail(email.getValue())
                .map(this.mapper::toDomain);
    }
    
    @Override
    public List<User> findAllActive() {
        return this.jpaRepository.findByActiveTrue()
                .stream()
                .map(this.mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<User> findWithPagination(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return this.jpaRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this.mapper::toDomain)
                .toList();
    }
    
    @Override
    public void delete(final UserId id) {
        Objects.requireNonNull(id, "UserId cannot be null");
        
        // Soft delete - apenas desativa o usuário
        this.jpaRepository.findById(id.getValue())
                .ifPresent(entity -> {
                    entity.setActive(false);
                    this.jpaRepository.save(entity);
                });
    }
    
    @Override
    public boolean existsByUsername(final Username username) {
        Objects.requireNonNull(username, "Username cannot be null");
        return this.jpaRepository.existsByUsername(username.getValue());
    }
    
    @Override
    public boolean existsByEmail(final Email email) {
        Objects.requireNonNull(email, "Email cannot be null");
        return this.jpaRepository.existsByEmail(email.getValue());
    }
    
    @Override
    public long countActive() {
        return this.jpaRepository.countByActiveTrue();
    }
}
