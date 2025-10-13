package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * JPA entity for Plant.
 */
@Entity
@Table(name = "plant")
public class PlantEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;
    
    // Default constructor required by JPA
    public PlantEntity() {
    }
    
    // Constructor for creating entity from domain object (via mapper)
    public PlantEntity(Long id, Long companyId, String name) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
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

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantEntity that = (PlantEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlantEntity{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", name='" + name + '\'' +
                '}';
    }
}
