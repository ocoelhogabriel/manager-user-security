package com.ocoelhogabriel.manager_user_security.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

/**
 * Entidade User seguindo Domain-Driven Design
 * Aplica Object Calisthenics:
 * - Regra 1: Only one level of indentation per method
 * - Regra 2: Don't use the ELSE keyword
 * - Regra 3: Wrap all primitives and Strings
 * - Regra 4: First class collections
 * - Regra 5: One dot per line
 * - Regra 6: Don't abbreviate
 * - Regra 9: No getters/setters/properties
 */
public final class User {
    private final UserId id;
    private final Username username;
    private final Email email;
    private final HashedPassword password;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(final UserId id, 
                final Username username, 
                final Email email, 
                final HashedPassword password, 
                final boolean active, 
                final LocalDateTime createdAt, 
                final LocalDateTime updatedAt) {
        this.validateConstructorParameters(username, email, password, createdAt);
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(final Username username, 
                             final Email email, 
                             final HashedPassword password) {
        final LocalDateTime now = LocalDateTime.now();
        return new User(null, username, email, password, true, now, now);
    }

    public static User restore(final UserId id, 
                              final Username username, 
                              final Email email, 
                              final HashedPassword password, 
                              final boolean active, 
                              final LocalDateTime createdAt, 
                              final LocalDateTime updatedAt) {
        return new User(id, username, email, password, active, createdAt, updatedAt);
    }

    public User deactivate() {
        return new User(this.id, this.username, this.email, this.password, 
                       false, this.createdAt, LocalDateTime.now());
    }

    public User activate() {
        return new User(this.id, this.username, this.email, this.password, 
                       true, this.createdAt, LocalDateTime.now());
    }

    public User changePassword(final HashedPassword newPassword) {
        return new User(this.id, this.username, this.email, newPassword, 
                       this.active, this.createdAt, LocalDateTime.now());
    }

    public User updateEmail(final Email newEmail) {
        return new User(this.id, this.username, newEmail, this.password, 
                       this.active, this.createdAt, LocalDateTime.now());
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

    public Username username() {
        return this.username;
    }

    public Email email() {
        return this.email;
    }

    public HashedPassword password() {
        return this.password;
    }

    public LocalDateTime createdAt() {
        return this.createdAt;
    }

    public LocalDateTime updatedAt() {
        return this.updatedAt;
    }

    private void validateConstructorParameters(final Username username, 
                                             final Email email, 
                                             final HashedPassword password, 
                                             final LocalDateTime createdAt) {
        if (Objects.isNull(username)) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (Objects.isNull(createdAt)) {
            throw new IllegalArgumentException("Created date cannot be null");
        }
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
        return Objects.equals(this.id, user.id) && 
               Objects.equals(this.username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", username=" + this.username +
                ", email=" + this.email +
                ", active=" + this.active +
                ", createdAt=" + this.createdAt +
                ", updatedAt=" + this.updatedAt +
                '}';
    }
}
