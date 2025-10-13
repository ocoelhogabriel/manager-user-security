package com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ocoelhogabriel.usersecurity.domain.entity.Role;
import com.ocoelhogabriel.usersecurity.domain.repository.RoleRepository;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.mapper.RoleMapper;
import com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.RoleRepository;

/**
 * Implementation of the RoleRepository interface.
 * This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class RoleRepositoryImpl implements RoleRepository {

    private final com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param roleRepository the JPA role repository
     * @param roleMapper the role mapper
     */
    public RoleRepositoryImpl(com.ocoelhogabriel.usersecurity.infrastructure.persistence.repository.RoleRepository roleRepository, 
                             RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Role save(Role role) {
        var roleEntity = roleMapper.toEntity(role);
        var savedEntity = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Role role) {
        roleRepository.deleteById(role.getId());
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name)
                .map(roleMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
    
    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }
}