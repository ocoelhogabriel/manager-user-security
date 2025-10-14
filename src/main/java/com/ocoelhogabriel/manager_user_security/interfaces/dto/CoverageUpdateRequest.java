package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating an existing Coverage.
 */
public class CoverageUpdateRequest {
    
    @JsonProperty("companyId")
    private Long companyId;
    
    @JsonProperty("plantId")
    private Long plantId;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("active")
    private Boolean active;

    // Default constructor
    public CoverageUpdateRequest() {
    }

    // All-args constructor
    public CoverageUpdateRequest(Long companyId, Long plantId, String description, Boolean active) {
        this.companyId = companyId;
        this.plantId = plantId;
        this.description = description;
        this.active = active;
    }

    // Getters and setters
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
