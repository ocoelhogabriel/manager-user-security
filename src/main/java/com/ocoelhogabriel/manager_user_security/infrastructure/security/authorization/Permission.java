package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

/**
 * Permission value object for the security module
 */
public record Permission(
        Long id,
        String name,
        String description,
        Resource resource,
        boolean canCreate,
        boolean canRead,
        boolean canList,
        boolean canEdit,
        boolean canDelete) {
    /**
     * Constructor for Permission.
     *
     * @param id          Permission ID
     * @param name        Permission name
     * @param description Permission description
     * @param resource    Associated resource
     */
    public Permission(Long id, String name, String description, Resource resource) {
        this(id, name, description, resource, true, true, true, true, true);
    }

    /**
     * Full constructor for Permission.
     *
     * @param id          Permission ID
     * @param name        Permission name
     * @param description Permission description
     * @param resource    Associated resource
     * @param canCreate   Create permission
     * @param canRead     Read permission
     * @param canList     List permission
     * @param canEdit     Edit permission
     * @param canDelete   Delete permission
     */
    public Permission {
    }
}