package com.ocoelhogabriel.manager_user_security.domain.entity;

import java.util.Objects;

/**
 * Coverage (Abrangência) domain entity that defines the scope of access a user has
 * to resources within the system. This determines which companies, plants, or other
 * organizational divisions a user can access.
 */
public class Coverage {
    private final Long id;
    private final User user;
    private final Company company;
    private final Plant plant;
    private final String description;
    private final boolean active;

    private Coverage(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.company = builder.company;
        this.plant = builder.plant;
        this.description = builder.description;
        this.active = builder.active;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Company getCompany() {
        return company;
    }

    public Plant getPlant() {
        return plant;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating Coverage instances.
     */
    public static class Builder {
        private Long id;
        private User user;
        private Company company;
        private Plant plant;
        private String description;
        private boolean active = true;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder company(Company company) {
            this.company = company;
            return this;
        }

        public Builder plant(Plant plant) {
            this.plant = plant;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Coverage build() {
            validateRequiredFields();
            return new Coverage(this);
        }

        private void validateRequiredFields() {
            if (user == null) {
                throw new IllegalArgumentException("User is required for Coverage");
            }
            
            if (company == null) {
                throw new IllegalArgumentException("Company is required for Coverage");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coverage coverage = (Coverage) o;
        return active == coverage.active &&
                Objects.equals(id, coverage.id) &&
                Objects.equals(user, coverage.user) &&
                Objects.equals(company, coverage.company) &&
                Objects.equals(plant, coverage.plant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, company, plant, active);
    }

    @Override
    public String toString() {
        return "Coverage{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", company=" + (company != null ? company.getId() : null) +
                ", plant=" + (plant != null ? plant.getId() : null) +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
