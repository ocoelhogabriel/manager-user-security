package com.ocoelhogabriel.usersecurity.interfaces.api.dto.role;

import java.util.List;

import com.ocoelhogabriel.usersecurity.interfaces.api.dto.permission.PermissionResponse;

/**
 * Data Transfer Object for role responses.
 */
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private List<PermissionResponse> permissions;

    /**
     * Default constructor.
     */
    public RoleResponse() {
    }

    /**
     * Constructor with basic fields.
     *
     * @param id the role ID
     * @param name the role name
     * @param description the role description
     */
    public RoleResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the ID.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the permissions.
     *
     * @return the permissions
     */
    public List<PermissionResponse> getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions.
     *
     * @param permissions the permissions to set
     */
    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }
}