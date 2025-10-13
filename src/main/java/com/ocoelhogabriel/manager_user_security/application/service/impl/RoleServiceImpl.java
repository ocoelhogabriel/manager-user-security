package com.ocoelhogabriel.manager_user_security.application.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.application.service.RoleService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.PermissionJpaRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository.RoleJpaRepository;

/**
 * Implementation of the RoleService interface.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleJpaRepository roleRepository;
    private final PermissionJpaRepository permissionRepository;
    private final ResourceService resourceService;
    
    /**
     * Constructor.
     *
     * @param roleRepository the role repository
     * @param permissionRepository the permission repository
     * @param resourceService the resource service
     */
    public RoleServiceImpl(
            RoleJpaRepository roleRepository, 
            PermissionJpaRepository permissionRepository,
            ResourceService resourceService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.resourceService = resourceService;
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
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        // Check if role with same name already exists
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            throw new DuplicateResourceException("Role", "name", role.getName());
        }
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Role role) {
        // Check if role exists
        if (!roleRepository.existsById(role.getId())) {
            throw new ResourceNotFoundException("Role", role.getId());
        }
        
        // Check if updated role would conflict with existing one
        Optional<Role> existingRole = roleRepository.findByNameAndIdNot(role.getName(), role.getId());
        if (existingRole.isPresent()) {
            throw new DuplicateResourceException("Role", "name", role.getName());
        }
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role", id);
        }
        
        // Delete related permissions first
        permissionRepository.deleteByRoleId(id);
        
        // Then delete the role
        roleRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Set<Role> findByUserId(Long userId) {
        List<Role> roles = roleRepository.findByUsersId(userId);
        return new HashSet<>(roles);
    }
    
    @Override
    @Transactional
    public Role addPermission(Long roleId, Long resourceId, String permissionName) {
        Role role = findById(roleId);
        Resource resource = resourceService.findById(resourceId);
        
        // Check if permission already exists
        Optional<Permission> existingPermission = 
                permissionRepository.findByRoleIdAndResourceId(roleId, resourceId);
        
        if (existingPermission.isPresent()) {
            // Update existing permission
            Permission permission = existingPermission.get();
            permission.setName(permissionName);
            permissionRepository.save(permission);
        } else {
            // Create new permission
            Permission permission = new Permission();
            permission.setName(permissionName);
            permission.setResource(resource);
            permission.setRole(role);
            permissionRepository.save(permission);
        }
        
        return role;
    }
    
    @Override
    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        // Verify role exists
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role", roleId);
        }
        
        // Verify permission exists
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));
        
        // Verify permission belongs to the role
        if (!permission.getRole().getId().equals(roleId)) {
            throw new ResourceNotFoundException("Permission with ID " + permissionId + 
                    " does not belong to role with ID " + roleId);
        }
        
        // Delete the permission
        permissionRepository.deleteById(permissionId);
    }
}
