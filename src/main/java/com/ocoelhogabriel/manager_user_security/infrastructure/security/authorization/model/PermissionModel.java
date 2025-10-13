package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.model;

import java.util.UUID;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;

/**
 * Enhanced domain Permission entity that provides compatibility for the various systems
 */
public class PermissionModel {
    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private ResourceModel resource;
    private Role role;
    private String action;
    
    /**
     * Creates a new PermissionModel with default values
     */
    public PermissionModel() {
        this.uuid = UUID.randomUUID();
    }
    
    /**
     * Creates a PermissionModel with specified values
     * 
     * @param id the ID
     * @param name the name
     * @param description the description
     * @param resource the resource
     */
    public PermissionModel(Long id, String name, String description, ResourceModel resource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.uuid = UUID.randomUUID();
    }
    
    /**
     * Creates a PermissionModel with specified values including role
     * 
     * @param id the ID
     * @param name the name
     * @param description the description
     * @param resource the resource
     * @param role the role
     */
    public PermissionModel(Long id, String name, String description, ResourceModel resource, Role role) {
        this(id, name, description, resource);
        this.role = role;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public ResourceModel getResource() {
        return resource;
    }
    
    public void setResource(ResourceModel resource) {
        this.resource = resource;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
}