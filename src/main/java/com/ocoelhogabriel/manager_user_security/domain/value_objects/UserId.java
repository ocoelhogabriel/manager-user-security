package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import java.util.Objects;

/**
 * Value Object para representar ID de usuário
 * Segue Object Calisthenics - Regra 3: Wrap all primitives and Strings
 */
public final class UserId {
    private final Long value;

    private UserId(final Long value) {
        this.validateValue(value);
        this.value = value;
    }

    public static UserId of(final Long value) {
        return new UserId(value);
    }

    public Long getValue() {
        return this.value;
    }

    private void validateValue(final Long value) {
        if (Objects.isNull(value) || value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
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
        final UserId userId = (UserId) obj;
        return Objects.equals(this.value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return "UserId{" +
                "value=" + this.value +
                '}';
    }
}
