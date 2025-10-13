package com.ocoelhogabriel.manager_user_security.application.dto.response;

/**
 * DTO for role information in responses
 */
public class RoleResponse {
    
    private String id;
    private String name;
    
    public RoleResponse() {
    }
    
    public RoleResponse(String id, String name) {
        this.id = id;
        this.name = name;
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
}