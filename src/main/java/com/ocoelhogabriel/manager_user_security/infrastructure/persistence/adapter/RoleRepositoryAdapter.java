package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.adapter;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.repository.RoleRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.mapper.RoleMapper;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.RoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository, RoleMapper roleMapper) {
        this.roleJpaRepository = roleJpaRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Role save(Role role) {
        var entity = roleMapper.toEntity(role);
        var savedEntity = roleJpaRepository.save(entity);
        return roleMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        roleJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return roleJpaRepository.existsById(id);
    }

    @Override
    public List<Role> findByActive(boolean active) {
        return roleJpaRepository.findByActive(active).stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Role> findByUserId(Long userId) {
        return roleJpaRepository.findByUsers_Id(userId).stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toSet());
    }
}
