package com.ocoelhogabriel.manager_user_security.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocoelhogabriel.manager_user_security.application.service.PermissionService;
import com.ocoelhogabriel.manager_user_security.application.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.RoleService;
import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.exception.DuplicateResourceException;
import com.ocoelhogabriel.manager_user_security.domain.exception.ResourceNotFoundException;
import com.ocoelhogabriel.manager_user_security.domain.repository.PermissionRepository;
import com.ocoelhogabriel.manager_user_security.domain.repository.RoleRepository;

/**
 * Implementation of the RoleService interface.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ResourceService resourceService;
    private final PermissionService permissionService;

    /**
     * Constructor.
     *
     * @param roleRepository the role repository
     * @param permissionRepository the permission repository
     * @param resourceService the resource service
     * @param permissionService the permission service
     */
    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            ResourceService resourceService,
            PermissionService permissionService) {
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
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElse(null);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        // Check if role with same name already exists
        if (roleRepository.existsByName(role.getName())) {
            throw new DuplicateResourceException("Role with name " + role.getName() + " already exists");
        }
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Role role) {
        // Verify role exists
        roleRepository.findById(role.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Role", role.getId()));

        // Check if role with same name already exists (excluding this one)
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
        // Check if role exists
        Role role = findById(roleId);

        // Check if resource exists
        Resource resource = resourceService.findById(resourceId);
        
        // Create a set with the single permission action
        Set<String> actions = new HashSet<>();
        actions.add(permissionName);

        // Create new permission
        Permission permission = new Permission(null, resource.getName(), actions);

        // Save permission and associate with role
        Permission savedPermission = permissionService.create(permission);

        // Return updated role
        return findById(roleId);
    }

    @Override
    @Transactional
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        // Check if role exists
        Role role = findById(roleId);

        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            // Get permission
            Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));

            permissions.add(permission);
        }

        // Update role with permissions (this is an example, the actual implementation will depend on your domain model)
        // In a real implementation, you'd update the role-permission relationships

        // Return updated role
        return findById(roleId);
    }

    @Override
    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        // Check if role exists
        Role role = findById(roleId);

        // Check if permission exists
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionId));

        // Remove permission from role (implementation depends on your domain model)
        // In a real implementation, you'd remove the role-permission relationship

        // For example:
        // rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }
}
