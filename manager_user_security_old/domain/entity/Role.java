package com.ocoelhogabriel.manager_user_security.domain.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Role entity representing a user role
 */
public class Role implements GrantedAuthority {
    private String id;
    private String name;
    
    public Role() {
    }
    
    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public String getAuthority() {
        return name;
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