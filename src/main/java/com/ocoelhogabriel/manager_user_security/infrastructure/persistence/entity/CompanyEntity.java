package com.ocoelhogabriel.manager_user_security.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * JPA entity for Company.
 */
@Entity
@Table(name = "company")
public class CompanyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "trading_name")
    private String tradingName;
    
    @Column(name = "phone")
    private String phone;
    
    // Default constructor required by JPA
    public CompanyEntity() {
    }
    
    // Constructor for creating entity from domain object (via mapper)
    public CompanyEntity(Long id, String cnpj, String name, String tradingName, String phone) {
        this.id = id;
        this.cnpj = cnpj;
        this.name = name;
        this.tradingName = tradingName;
        this.phone = phone;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity that = (CompanyEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", tradingName='" + tradingName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
