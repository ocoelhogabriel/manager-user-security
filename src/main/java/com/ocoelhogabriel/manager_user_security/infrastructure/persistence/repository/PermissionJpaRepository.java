package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.ResourceEntity;
import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity.PermissionEntity;

/**
 * Repository for managing Permission entities.
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {

    /**
     * Find permissions by role ID.
     *
     * @param roleId the role ID
     * @return the permissions
     */
    List<PermissionEntity> findByRoleId(Long roleId);
    
    /**
     * Find permissions by resource ID.
     *
     * @param resourceId the resource ID
     * @return the permissions
     */
    List<PermissionEntity> findByResourceId(Long resourceId);

    /**
     * Find permissions by resource name.
     *
     * @param resourceName the resource name
     * @return the permissions
     */
    List<PermissionEntity> findByResourceName(String resourceName);

    /**
     * Find a permission by resource name and action.
     *
     * @param resourceName the resource name
     * @param action the action
     * @return the permission if found
     */
    Optional<PermissionEntity> findByResourceNameAndAction(String resourceName, String action);

    /**
     * Check if a permission exists by resource name and action.
     *
     * @param resourceName the resource name
     * @param action the action
     * @return true if the permission exists, false otherwise
     */
    boolean existsByResourceNameAndAction(String resourceName, String action);

    /**
     * Find a permission by role and resource.
     *
     * @param role the role entity
     * @param resource the resource entity
     * @return the permission if found
     */
    Optional<PermissionEntity> findByRoleAndResource(RoleEntity role, ResourceEntity resource);

    /**
     * Find permissions by role IDs and resource ID.
     *
     * @param roleIds the list of role IDs
     * @param resourceId the resource ID
     * @return the permissions
     */
    List<PermissionEntity> findByRoleIdInAndResourceId(List<Long> roleIds, Long resourceId);

    Optional<PermissionEntity> findByRoleIdInAndResourceIdIn(List<Long> roleIds, List<Long> resourceIds);
}
