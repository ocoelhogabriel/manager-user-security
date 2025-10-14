package com.ocoelhogabriel.manager_user_security.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Response DTO for Coverage entities.
 */
public class CoverageResponse {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("companyId")
    private Long companyId;
    
    @JsonProperty("companyName")
    private String companyName;
    
    @JsonProperty("plantId")
    private Long plantId;
    
    @JsonProperty("plantName")
    private String plantName;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("active")
    private boolean active;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    // Default constructor
    public CoverageResponse() {
    }

    // Builder pattern for constructing the response
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CoverageResponse response = new CoverageResponse();
        
        public Builder id(Long id) {
            response.id = id;
            return this;
        }
        
        public Builder userId(Long userId) {
            response.userId = userId;
            return this;
        }
        
        public Builder username(String username) {
            response.username = username;
            return this;
        }
        
        public Builder companyId(Long companyId) {
            response.companyId = companyId;
            return this;
        }
        
        public Builder companyName(String companyName) {
            response.companyName = companyName;
            return this;
        }
        
        public Builder plantId(Long plantId) {
            response.plantId = plantId;
            return this;
        }
        
        public Builder plantName(String plantName) {
            response.plantName = plantName;
            return this;
        }
        
        public Builder description(String description) {
            response.description = description;
            return this;
        }
        
        public Builder active(boolean active) {
            response.active = active;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            response.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            response.updatedAt = updatedAt;
            return this;
        }
        
        public CoverageResponse build() {
            return response;
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Long getPlantId() {
        return plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
