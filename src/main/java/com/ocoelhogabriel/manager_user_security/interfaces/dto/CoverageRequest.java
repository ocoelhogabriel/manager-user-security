package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new Coverage.
 */
public class CoverageRequest {
    
    @NotNull(message = "User ID is required")
    @JsonProperty("userId")
    private Long userId;
    
    @NotNull(message = "Company ID is required")
    @JsonProperty("companyId")
    private Long companyId;
    
    @JsonProperty("plantId")
    private Long plantId;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("active")
    private boolean active = true;

    // Default constructor
    public CoverageRequest() {
    }

    // All-args constructor
    public CoverageRequest(Long userId, Long companyId, Long plantId, String description, boolean active) {
        this.userId = userId;
        this.companyId = companyId;
        this.plantId = plantId;
        this.description = description;
        this.active = active;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
