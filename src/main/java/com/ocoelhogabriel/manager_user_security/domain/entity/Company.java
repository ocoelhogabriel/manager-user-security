package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.Objects;

/**
 * Company domain entity representing a business organization.
 * This is a pure domain entity, independent of any framework or infrastructure concerns.
 */
public class Company {
    
    private Long id;
    private String cnpj;
    private String name;
    private String tradingName;
    private String phone;

    public Company() {
    }
    
    // Private constructor for builder pattern
    private Company(Builder builder) {
        this.id = builder.id;
        this.cnpj = builder.cnpj;
        this.name = builder.name;
        this.tradingName = builder.tradingName;
        this.phone = builder.phone;
    }
    
    // Getters only - immutable entity
    public Long getId() {
        return id;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public String getName() {
        return name;
    }
    
    public String getTradingName() {
        return tradingName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    // Domain logic methods
    public boolean isValid() {
        return cnpj != null && !cnpj.isBlank() &&
               name != null && !name.isBlank();
    }
    
    // Builder pattern for Company
    public static class Builder {
        private Long id;
        private String cnpj;
        private String name;
        private String tradingName;
        private String phone;
        
        public Builder withId(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder withCnpj(String cnpj) {
            this.cnpj = cnpj;
            return this;
        }
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withTradingName(String tradingName) {
            this.tradingName = tradingName;
            return this;
        }
        
        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Company build() {
            return new Company(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(cnpj, company.cnpj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(cnpj);
    }
    
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", tradingName='" + tradingName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
