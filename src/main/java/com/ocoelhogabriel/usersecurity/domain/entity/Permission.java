package com.ocoelhogabriel.usersecurity.domain.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Collections;

/**
 * Permission domain entity representing a permission in the system.
 * Permissions are used to control access to resources.
 */
public class Permission {
    private final UUID id;
    private String resource;
    private Set<String> actions;

    /**
     * Creates a new Permission with the specified resource and action.
     *
     * @param resource the resource name
     * @param action the action to be performed on the resource
     */
    public Permission(String resource, String action) {
        this.id = UUID.randomUUID();
        this.resource = resource;
        this.actions = new HashSet<>();
        this.actions.add(action);
    }

    /**
     * Creates a Permission with an existing ID.
     *
     * @param id the ID of the permission
     * @param resource the resource name
     * @param actions the actions that can be performed on the resource
     */
    public Permission(UUID id, String resource, Set<String> actions) {
        this.id = id;
        this.resource = resource;
        this.actions = actions != null ? new HashSet<>(actions) : new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Set<String> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    /**
     * Adds an action to the permission.
     *
     * @param action the action to add
     * @return true if the action was added, false if it was already present
     */
    public boolean addAction(String action) {
        return actions.add(action);
    }

    /**
     * Removes an action from the permission.
     *
     * @param action the action to remove
     * @return true if the action was removed, false if it was not present
     */
    public boolean removeAction(String action) {
        return actions.remove(action);
    }

    /**
     * Checks if the permission has a specific action.
     *
     * @param action the action to check
     * @return true if the permission has the action, false otherwise
     */
    public boolean hasAction(String action) {
        return actions.contains(action);
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
}