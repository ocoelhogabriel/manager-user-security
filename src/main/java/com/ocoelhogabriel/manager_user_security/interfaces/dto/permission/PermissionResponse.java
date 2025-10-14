package com.ocoelhogabriel.manager_user_security.interfaces.dto.permission;

import com.ocoelhogabriel.manager_user_security.interfaces.dto.resource.ResourceResponse;

/**
 * Data Transfer Object for permission responses.
 */
public class PermissionResponse {

    private Long id;
    private String name;
    private String description;
    private ResourceResponse resource;
    private Long roleId;
    private String roleName;

    /**
     * Default constructor.
     */
    public PermissionResponse() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the permission ID
     * @param name the permission name
     * @param description the permission description
     * @param resource the associated resource
     * @param roleId the role ID
     * @param roleName the role name
     */
    public PermissionResponse(Long id, String name, String description, 
            ResourceResponse resource, Long roleId, String roleName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.roleId = roleId;
        this.roleName = roleName;
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
     * Gets the resource.
     *
     * @return the resource
     */
    public ResourceResponse getResource() {
        return resource;
    }

    /**
     * Sets the resource.
     *
     * @param resource the resource to set
     */
    public void setResource(ResourceResponse resource) {
        this.resource = resource;
    }

    /**
     * Gets the role ID.
     *
     * @return the roleId
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID.
     *
     * @param roleId the roleId to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Gets the role name.
     *
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the role name.
     *
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
