package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Permission domain entity. Refactored to a standard POJO for framework compatibility.
 */
public class Permission {
    private Long id;
    private String resource;
    private Set<String> actions = new HashSet<>();

    /**
     * No-argument constructor for frameworks like JPA and MapStruct.
     */
    public Permission() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Set<String> getActions() {
        return actions;
    }

    public void setActions(Set<String> actions) {
        this.actions = actions != null ? new HashSet<>(actions) : new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean hasAction(String action) {
        return actions.contains(action);
    }
}
