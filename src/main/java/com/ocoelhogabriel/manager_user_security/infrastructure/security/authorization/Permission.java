package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization;

/**
 * Permission value object for the security module
 */
public class Permission {
    private final Long id;
    private final String name;
    private final String description;
    private final Resource resource;
    private final boolean canCreate;
    private final boolean canRead;
    private final boolean canList;
    private final boolean canEdit;
    private final boolean canDelete;
    
    /**
     * Constructor for Permission.
     *
     * @param id Permission ID
     * @param name Permission name
     * @param description Permission description
     * @param resource Associated resource
     */
    public Permission(Long id, String name, String description, Resource resource) {
        this(id, name, description, resource, true, true, true, true, true);
    }
    
    /**
     * Full constructor for Permission.
     *
     * @param id Permission ID
     * @param name Permission name
     * @param description Permission description
     * @param resource Associated resource
     * @param canCreate Create permission
     * @param canRead Read permission
     * @param canList List permission
     * @param canEdit Edit permission
     * @param canDelete Delete permission
     */
    public Permission(Long id, String name, String description, Resource resource, 
                     boolean canCreate, boolean canRead, boolean canList, 
                     boolean canEdit, boolean canDelete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.canCreate = canCreate;
        this.canRead = canRead;
        this.canList = canList;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Resource getResource() {
        return resource;
    }
    
    public boolean canCreate() {
        return canCreate;
    }
    
    public boolean canRead() {
        return canRead;
    }
    
    public boolean canList() {
        return canList;
    }
    
    public boolean canEdit() {
        return canEdit;
    }
    
    public boolean canDelete() {
        return canDelete;
    }
}