package com.ocoelhogabriel.manager_user_security.domain.entity;

/**
 * Resource entity representing a protected resource in the system
 */
public class Resource {
    private String id;
    private String name;
    private String description;
    
    public Resource() {
    }
    
    public Resource(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Resource(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}