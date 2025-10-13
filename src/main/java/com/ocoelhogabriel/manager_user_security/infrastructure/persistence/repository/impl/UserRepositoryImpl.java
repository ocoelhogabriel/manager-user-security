package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.repository.UserRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.UserEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.UserMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.UserJpaRepository;

/**
 * Implementation of the UserRepository interface.
 * This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Constructor.
     *
     * @param userRepository the JPA user repository
     * @param userMapper the user mapper
     */
    public UserRepositoryImpl(UserJpaRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(User user) {
        userRepository.deleteById(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
