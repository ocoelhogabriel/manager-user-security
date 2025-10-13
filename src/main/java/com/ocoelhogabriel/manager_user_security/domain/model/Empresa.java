package com.ocoelhogabriel.manager_user_security.domain.model;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.CNPJ;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.EmpresaId;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rich domain entity for Empresa (Company).
 * This class is immutable; all "modifications" return a new instance.
 * Construction is managed by the Builder pattern to ensure consistency.
 */
public final class Empresa {

    private final EmpresaId id;
    private final CNPJ cnpj;
    private final Name name;
    private final String fantasyName;
    private final String phone;
    private final boolean active; // Assuming an active status is needed
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Empresa(EmpresaBuilder builder) {
        this.id = builder.id;
        this.cnpj = builder.cnpj;
        this.name = builder.name;
        this.fantasyName = builder.fantasyName;
        this.phone = builder.phone;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static EmpresaBuilder builder() {
        return new EmpresaBuilder();
    }

    public Empresa updateInfo(Name newName, String newFantasyName, String newPhone) {
        return Empresa.builder().from(this)
            .name(newName)
            .fantasyName(newFantasyName)
            .phone(newPhone)
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public boolean hasId() {
        return Objects.nonNull(this.id);
    }

    // --- Getters ---
    public EmpresaId id() { return id; }
    public CNPJ cnpj() { return cnpj; }
    public Name name() { return name; }
    public String fantasyName() { return fantasyName; }
    public String phone() { return phone; }
    public boolean isActive() { return active; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(id, empresa.id) || Objects.equals(cnpj, empresa.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cnpj);
    }

    public static class EmpresaBuilder {
        private EmpresaId id;
        private CNPJ cnpj;
        private Name name;
        private String fantasyName;
        private String phone;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private EmpresaBuilder() {}

        public EmpresaBuilder from(Empresa empresa) {
            this.id = empresa.id;
            this.cnpj = empresa.cnpj;
            this.name = empresa.name;
            this.fantasyName = empresa.fantasyName;
            this.phone = empresa.phone;
            this.active = empresa.active;
            this.createdAt = empresa.createdAt;
            this.updatedAt = empresa.updatedAt;
            return this;
        }

        public EmpresaBuilder id(EmpresaId id) {
            this.id = id;
            return this;
        }

        public EmpresaBuilder cnpj(CNPJ cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public EmpresaBuilder name(Name name) {
            this.name = name;
            return this;
        }

        public EmpresaBuilder fantasyName(String fantasyName) {
            this.fantasyName = fantasyName;
            return this;
        }

        public EmpresaBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public EmpresaBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public EmpresaBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmpresaBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Empresa build() {
            validate();
            return new Empresa(this);
        }

        private void validate() {
            if (Objects.isNull(cnpj)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "cnpj"));
            }
            if (Objects.isNull(name)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "name"));
            }
            if (Objects.isNull(createdAt)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "createdAt"));
            }
        }
    }
}
