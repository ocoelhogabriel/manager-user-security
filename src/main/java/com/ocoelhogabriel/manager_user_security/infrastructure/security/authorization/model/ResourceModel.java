package com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.model;

import java.util.UUID;

/**
 * Enhanced domain Resource entity that provides compatibility for the various systems
 */
public class ResourceModel {
    private Long id;
    private String name;
    private String description;
    private String path;
    private String method;
    private UUID uuid;
    
    /**
     * Creates a new ResourceModel with default values
     */
    public ResourceModel() {
        this.uuid = UUID.randomUUID();
    }
    
    /**
     * Creates a ResourceModel with specified values
     * 
     * @param id the ID
     * @param name the name
     * @param description the description
     * @param path the path
     * @param method the method
     */
    public ResourceModel(Long id, String name, String description, String path, String method) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
        this.method = method;
        this.uuid = UUID.randomUUID();
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
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
}