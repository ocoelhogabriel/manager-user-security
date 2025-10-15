package com.ocoelhogabriel.manager_user_security.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.manager_user_security.domain.service.PermissionService;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.RoleService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ResourceService resourceService;
    private final PermissionService permissionService;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            @Qualifier("resourceServiceImpl") ResourceService resourceService, // Corrigido
            @Qualifier("permissionServiceImpl") PermissionService permissionService) { // Corrigido
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.resourceService = resourceService;
        this.permissionService = permissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DuplicateResourceException("Role with name " + role.getName() + " already exists");
        }
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Role role) {
        roleRepository.findById(role.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Role", role.getId()));

        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent() && !existingRole.get().getId().equals(role.getId())) {
            throw new DuplicateResourceException("Role with name " + role.getName() + " already exists");
        }
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role", id);
        }
        
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findByActive() {
        return new HashSet<>(roleRepository.findByActive(true));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findByUserId(Long userId) {
        return roleRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Role addPermission(Long roleId, Long resourceId, String permissionName) {
        Role role = findById(roleId);
        Resource resource = resourceService.findById(resourceId);
        
        Set<String> actions = new HashSet<>();
        actions.add(permissionName);

        // Corrigido: Usando construtor padrão e setters
        Permission permission = new Permission();
        permission.setResource(resource.getName());
        permission.setActions(actions);

        permissionService.create(permission);

        return findById(roleId);
    }

    @Override
    @Transactional
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = findById(roleId);

        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));

            permissions.add(permission);
        }

        // A lógica de associação real precisaria ser implementada aqui

        return findById(roleId);
    }

    @Override
    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        findById(roleId);
        permissionRepository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));

        // A lógica de remoção real precisaria ser implementada aqui
    }
}
