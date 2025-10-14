package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object for user response.
 */
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private Set<RoleResponse> roles;
    
    public UserResponse() {
        this.roles = new HashSet<>();
    }
    
    public UserResponse(Long id, String username, String email, boolean active, Set<RoleResponse> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.roles = roles != null ? roles : new HashSet<>();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Set<RoleResponse> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<RoleResponse> roles) {
        this.roles = roles;
    }
    
    /**
     * Inner class for role response.
     */
    public static class RoleResponse {
        private Long id;
        private String name;
        private String description;
        
        public RoleResponse() {
        }
        
        public RoleResponse(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
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
}
