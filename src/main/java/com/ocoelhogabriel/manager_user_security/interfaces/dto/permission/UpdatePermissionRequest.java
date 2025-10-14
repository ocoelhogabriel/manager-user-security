package com.ocoelhogabriel.manager_user_security.interfaces.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating an existing permission.
 */
public class UpdatePermissionRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Resource ID is required")
    private Long resourceId;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action cannot exceed 50 characters")
    private String action;

    @NotBlank(message = "Resource name is required")
    @Size(max = 100, message = "Resource name cannot exceed 100 characters")
    private String resourceName;

    /**
     * Default constructor.
     */
    public UpdatePermissionRequest() {
    }

    /**
     * Constructor with all fields.
     *
     * @param name the permission name
     * @param description the permission description
     * @param resourceId the resource ID
     * @param roleId the role ID
     * @param action the action
     * @param resourceName the resource name
     */
    public UpdatePermissionRequest(String name, String description, Long resourceId, Long roleId,
                                  String action, String resourceName) {
        this.name = name;
        this.description = description;
        this.resourceId = resourceId;
        this.roleId = roleId;
        this.action = action;
        this.resourceName = resourceName;
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
     * Gets the resource ID.
     *
     * @return the resource ID
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * Sets the resource ID.
     *
     * @param resourceId the resource ID to set
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Gets the role ID.
     *
     * @return the role ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID.
     *
     * @param roleId the role ID to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Gets the resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the resource name.
     *
     * @param resourceName the resource name to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
