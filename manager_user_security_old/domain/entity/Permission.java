package com.ocoelhogabriel.manager_user_security.domain.entity;

/**
 * Permission entity representing permissions for a role on a specific resource
 */
public class Permission {
    private String id;
    private Role role;
    private Resource resource;
    private boolean canList;
    private boolean canRead;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;
    
    public Permission() {
    }
    
    public Permission(String id, Role role, Resource resource, 
                     boolean canList, boolean canRead, boolean canCreate, 
                     boolean canEdit, boolean canDelete) {
        this.id = id;
        this.role = role;
        this.resource = resource;
        this.canList = canList;
        this.canRead = canRead;
        this.canCreate = canCreate;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public boolean canList() {
        return canList;
    }

    public void setCanList(boolean canList) {
        this.canList = canList;
    }

    public boolean canRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean canCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean canEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean canDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}