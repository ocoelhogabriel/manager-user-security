package com.ocoelhogabriel.manager_user_security.domain.repository;

import java.util.List;
import java.util.Optional;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Repository interface for Permission entities. Extends the generic Repository interface with Permission-specific methods.
 */
public interface PermissionRepository extends Repository<Permission, Long> {

    /**
     * Finds permissions by resource.
     *
     * @param resource the resource to search for
     * @return a list of permissions for the given resource
     */
    List<Permission> findByResource(String resource);

    /**
     * Finds a permission by resource and action.
     *
     * @param resource the resource to search for
     * @param action   the action to search for
     * @return an Optional containing the found permission, or empty if not found
     */
    Optional<Permission> findByResourceAndAction(String resource, String action);

    /**
     * Checks if a permission exists for the given resource and action.
     *
     * @param resource the resource name
     * @param action   the action name
     * @return true if a permission exists, false otherwise
     */
    boolean existsByResourceAndAction(String resource, String action);

    /**
     * Finds permissions by role ID.
     *
     * @param roleId the role ID
     * @return a list of permissions for the given role
     */
    List<Permission> findByRoleId(Long roleId);

    /**
     * Finds a permission by role and resource.
     *
     * @param role     the role
     * @param resource the resource
     * @return an Optional containing the found permission, or empty if not found
     */
    Optional<Permission> findByRoleAndResource(Role role, Resource resource);

    /**
     * Finds permissions by role IDs and resource ID.
     *
     * @param roleIds    the list of role IDs
     * @param resourceId the resource ID
     * @return a list of permissions matching the criteria
     */
    List<Permission> findByRoleIdsAndResourceId(List<Long> roleIds, Long resourceId);
}
