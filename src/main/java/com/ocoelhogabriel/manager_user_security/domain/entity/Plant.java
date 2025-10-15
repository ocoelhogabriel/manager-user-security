package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.Objects;

/**
 * Plant domain entity. Refactored to a standard POJO for framework compatibility.
 */
public class Plant {

    private Long id;
    private Long companyId;
    private String name;

    /**
     * Default constructor for frameworks like JPA and MapStruct.
     */
    public Plant(Builder builder) {
    }

    // Getters and Setters
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

    // Domain logic methods
    public boolean isValid() {
        return companyId != null && companyId > 0 && name != null && !name.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Plant{" + "id=" + id + ", companyId=" + companyId + ", name='" + name + "\'" + '}';
    }

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
}
