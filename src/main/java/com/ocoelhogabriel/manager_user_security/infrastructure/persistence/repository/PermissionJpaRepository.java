package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

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
     * Find a permission by role ID and resource ID.
     *
     * @param roleId the role ID
     * @param resourceId the resource ID
     * @return the permission, if found
     */
    Optional<PermissionEntity> findByRoleIdAndResourceId(Long roleId, Long resourceId);
    
    /**
     * Find a permission by role IDs and resource IDs.
     *
     * @param roleIds the role IDs
     * @param resourceIds the resource IDs
     * @return the permission, if found
     */
    @Query("SELECT p FROM PermissionEntity p WHERE p.role.id IN :roleIds AND p.resource.id IN :resourceIds")
    Optional<PermissionEntity> findByRoleIdInAndResourceIdIn(
            @Param("roleIds") List<Long> roleIds, 
            @Param("resourceIds") List<Long> resourceIds);
    
    /**
     * Delete permissions by role ID.
     *
     * @param roleId the role ID
     */
    void deleteByRoleId(Long roleId);
    
    /**
     * Delete permissions by resource ID.
     *
     * @param resourceId the resource ID
     */
    void deleteByResourceId(Long resourceId);
}
