package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.RoleRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.RoleMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.RoleJpaRepository;

/**
 * Implementation of the RoleRepository interface. This class adapts the Spring Data JPA repository to the domain repository interface.
 */
@Component
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Constructor.
     *
     * @param roleRepository the JPA role repository
     * @param roleMapper     the role mapper
     */
    public RoleRepositoryImpl(RoleJpaRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Role save(RoleEntity role) {
        var roleEntity = roleMapper.toEntity(role);
        var savedEntity = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id).map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name).map(roleMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public List<Role> findByActive(boolean active) {
        return roleRepository.findByActive(active).stream().map(roleMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Set<Role> findByUserId(Long userId) {
        return roleRepository.findByUserId(userId).stream().map(roleMapper::toDomain).collect(Collectors.toSet());
    }
}
