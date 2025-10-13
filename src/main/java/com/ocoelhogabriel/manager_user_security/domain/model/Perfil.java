package com.ocoelhogabriel.manager_user_security.domain.model;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Name;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.NivelAcesso;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.PerfilId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rich domain entity for Perfil (Profile).
 * This class is immutable; all "modifications" return a new instance.
 * Construction is managed by the Builder pattern to ensure consistency.
 */
public final class Perfil {

    private final PerfilId id;
    private final Name nome;
    private final String descricao;
    private final NivelAcesso nivelAcesso;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Perfil(PerfilBuilder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.descricao = builder.descricao;
        this.nivelAcesso = builder.nivelAcesso;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static PerfilBuilder builder() {
        return new PerfilBuilder();
    }

    public Perfil deactivate() {
        return Perfil.builder().from(this).active(false).updatedAt(LocalDateTime.now()).build();
    }

    public Perfil activate() {
        return Perfil.builder().from(this).active(true).updatedAt(LocalDateTime.now()).build();
    }

    public Perfil updateInfo(Name newName, String newDescription) {
        return Perfil.builder().from(this)
            .nome(newName)
            .descricao(newDescription)
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public boolean hasId() {
        return Objects.nonNull(this.id);
    }

    // --- Getters ---
    public PerfilId id() { return id; }
    public Name nome() { return nome; }
    public String descricao() { return descricao; }
    public NivelAcesso nivelAcesso() { return nivelAcesso; }
    public boolean isActive() { return active; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(id, perfil.id) || Objects.equals(nome, perfil.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    public static class PerfilBuilder {
        private PerfilId id;
        private Name nome;
        private String descricao;
        private NivelAcesso nivelAcesso;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private PerfilBuilder() {}

        public PerfilBuilder from(Perfil perfil) {
            this.id = perfil.id;
            this.nome = perfil.nome;
            this.descricao = perfil.descricao;
            this.nivelAcesso = perfil.nivelAcesso;
            this.active = perfil.active;
            this.createdAt = perfil.createdAt;
            this.updatedAt = perfil.updatedAt;
            return this;
        }

        public PerfilBuilder id(PerfilId id) {
            this.id = id;
            return this;
        }

        public PerfilBuilder nome(Name nome) {
            this.nome = nome;
            return this;
        }

        public PerfilBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public PerfilBuilder nivelAcesso(NivelAcesso nivelAcesso) {
            this.nivelAcesso = nivelAcesso;
            return this;
        }

        public PerfilBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public PerfilBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PerfilBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Perfil build() {
            validate();
            return new Perfil(this);
        }

        private void validate() {
            if (Objects.isNull(nome)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "nome"));
            }
            if (Objects.isNull(nivelAcesso)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "nivelAcesso"));
            }
            if (Objects.isNull(createdAt)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "createdAt"));
            }
        }
    }
}
