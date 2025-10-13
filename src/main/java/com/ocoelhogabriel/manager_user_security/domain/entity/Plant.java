package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.Objects;

/**
 * Plant domain entity representing a physical location of a company.
 * This is a pure domain entity, independent of any framework or infrastructure concerns.
 */
public class Plant {
    
    private Long id;
    private Long companyId;
    private String name;
    
    // Private constructor for builder pattern
    private Plant(Builder builder) {
        this.id = builder.id;
        this.companyId = builder.companyId;
        this.name = builder.name;
    }
    
    // Getters only - immutable entity
    public Long getId() {
        return id;
    }
    
    public Long getCompanyId() {
        return companyId;
    }
    
    public String getName() {
        return name;
    }
    
    // Domain logic methods
    public boolean isValid() {
        return companyId != null && companyId > 0 &&
               name != null && !name.isBlank();
    }
    
    // Builder pattern for Plant
    public static class Builder {
        private Long id;
        private Long companyId;
        private String name;
        
        public Builder withId(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder withCompanyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Plant build() {
            return new Plant(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", name='" + name + '\'' +
                '}';
    }
}
