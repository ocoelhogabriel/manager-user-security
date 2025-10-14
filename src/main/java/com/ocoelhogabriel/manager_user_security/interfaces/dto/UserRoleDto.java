package com.ocoelhogabriel.manager_user_security.interfaces.dto;

/**
 * Data Transfer Object for user role information.
 */
public class UserRoleDto {
    private String id;
    private String name;
    private String description;
    
    public UserRoleDto() {
    }
    
    public UserRoleDto(String id, String name, String description) {
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
