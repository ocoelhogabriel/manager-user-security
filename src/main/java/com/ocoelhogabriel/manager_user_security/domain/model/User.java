package com.ocoelhogabriel.manager_user_security.domain.model;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade User seguindo Domain-Driven Design.
 * A classe é imutável; todas as "modificações" retornam uma nova instância.
 * A construção é gerenciada pelo padrão Builder para garantir consistência e legibilidade.
 */
public final class User {

    private final UserId id;
    private final Name name;
    private final CPF cpf;
    private final Username username;
    private final Email email;
    private final HashedPassword password;
    private final EmpresaId empresaId;
    private final PerfilId perfilId;
    private final AbrangenciaId abrangenciaId;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.cpf = builder.cpf;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.empresaId = builder.empresaId;
        this.perfilId = builder.perfilId;
        this.abrangenciaId = builder.abrangenciaId;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User deactivate() {
        return User.builder().from(this).active(false).updatedAt(LocalDateTime.now()).build();
    }

    public User activate() {
        return User.builder().from(this).active(true).updatedAt(LocalDateTime.now()).build();
    }

    public User changePassword(final HashedPassword newPassword) {
        return User.builder().from(this).password(newPassword).updatedAt(LocalDateTime.now()).build();
    }

    public User updateEmail(final Email newEmail) {
        return User.builder().from(this).email(newEmail).updatedAt(LocalDateTime.now()).build();
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean hasId() {
        return Objects.nonNull(this.id);
    }

    public UserId id() {
        return this.id;
    }

    public Name name() {
        return this.name;
    }

    public CPF cpf() {
        return this.cpf;
    }

    public Username username() {
        return this.username;
    }

    public Email email() {
        return this.email;
    }

    public HashedPassword password() {
        return this.password;
    }

    public EmpresaId empresaId() {
        return this.empresaId;
    }

    public PerfilId perfilId() {
        return this.perfilId;
    }

    public AbrangenciaId abrangenciaId() {
        return this.abrangenciaId;
    }

    public LocalDateTime createdAt() {
        return this.createdAt;
    }

    public LocalDateTime updatedAt() {
        return this.updatedAt;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        return Objects.equals(this.id, user.id) || Objects.equals(this.cpf, user.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.cpf);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name=" + name +
            ", cpf=" + cpf +
            ", username=" + username +
            ", email=" + email +
            ", empresaId=" + empresaId +
            ", perfilId=" + perfilId +
            ", abrangenciaId=" + abrangenciaId +
            ", password='[PROTECTED]'" +
            ", active=" + active +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }

    public static class UserBuilder {
        private UserId id;
        private Name name;
        private CPF cpf;
        private Username username;
        private Email email;
        private HashedPassword password;
        private EmpresaId empresaId;
        private PerfilId perfilId;
        private AbrangenciaId abrangenciaId;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private UserBuilder() {}

        public UserBuilder from(User user) {
            this.id = user.id;
            this.name = user.name;
            this.cpf = user.cpf;
            this.username = user.username;
            this.email = user.email;
            this.password = user.password;
            this.empresaId = user.empresaId;
            this.perfilId = user.perfilId;
            this.abrangenciaId = user.abrangenciaId;
            this.active = user.active;
            this.createdAt = user.createdAt;
            this.updatedAt = user.updatedAt;
            return this;
        }

        public UserBuilder id(UserId id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(Name name) {
            this.name = name;
            return this;
        }

        public UserBuilder cpf(CPF cpf) {
            this.cpf = cpf;
            return this;
        }

        public UserBuilder username(Username username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(Email email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(HashedPassword password) {
            this.password = password;
            return this;
        }

        public UserBuilder empresaId(EmpresaId empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public UserBuilder perfilId(PerfilId perfilId) {
            this.perfilId = perfilId;
            return this;
        }

        public UserBuilder abrangenciaId(AbrangenciaId abrangenciaId) {
            this.abrangenciaId = abrangenciaId;
            return this;
        }

        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public User build() {
            validate();
            return new User(this);
        }

        private void validate() {
            if (Objects.isNull(name)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "name"));
            }
            if (Objects.isNull(cpf)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "cpf"));
            }
            if (Objects.isNull(username)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "username"));
            }
            if (Objects.isNull(email)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "email"));
            }
            if (Objects.isNull(password)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "password"));
            }
            if (Objects.isNull(empresaId)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "empresaId"));
            }
            if (Objects.isNull(perfilId)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "perfilId"));
            }
            if (Objects.isNull(abrangenciaId)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "abrangenciaId"));
            }
            if (Objects.isNull(createdAt)) {
                throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                    MessageConstraints.VALIDATION_REQUIRED_FIELD, "createdAt"));
            }
        }
    }
}
