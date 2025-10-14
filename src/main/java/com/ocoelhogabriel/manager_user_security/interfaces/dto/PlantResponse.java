package com.ocoelhogabriel.manager_user_security.interfaces.dto;

/**
 * DTO for plant responses.
 */
public class PlantResponse {
    
    private Long id;
    private Long companyId;
    private String name;
    private String companyName; // Additional field for convenience
    
    // Constructors
    public PlantResponse() {
    }
    
    public PlantResponse(Long id, Long companyId, String name) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
    }
    
    public PlantResponse(Long id, Long companyId, String name, String companyName) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.companyName = companyName;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
