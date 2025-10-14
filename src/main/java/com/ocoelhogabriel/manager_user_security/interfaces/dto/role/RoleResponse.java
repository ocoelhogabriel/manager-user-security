package com.ocoelhogabriel.manager_user_security.interfaces.dto.role;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.permission.PermissionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object for role responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse {

    @Schema(description = "Role ID")
    private Long id;
    
    @Schema(description = "Role name", example = "ADMIN")
    private String name;
    
    @Schema(description = "Role description", example = "System administrator with full access")
    private String description;
    
    @Schema(description = "Whether the role is active", example = "true")
    private boolean active;
    
    @Schema(description = "Role code (optional identifier)", example = "SYS_ADMIN")
    private String code;
    
    @Schema(description = "When the role was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "When the role was last updated")
    private LocalDateTime updatedAt;
    
    @Schema(description = "List of permissions assigned to this role")
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
     * @param active whether the role is active
     * @param code the role code
     */
    public RoleResponse(Long id, String name, String description, boolean active, String code) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.code = code;
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
     * Gets the active status.
     *
     * @return the active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status.
     *
     * @param active the active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the created timestamp.
     *
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created timestamp.
     *
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the updated timestamp.
     *
     * @return the updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updated timestamp.
     *
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
