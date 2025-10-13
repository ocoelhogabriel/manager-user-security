package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import java.util.Objects;

/**
 * Value Object para representar nome de usuário
 * Segue Object Calisthenics - Regra 3: Wrap all primitives and Strings
 * Aplica SRP - responsável apenas por validação de nome de usuário
 */
public final class Username {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    
    private final String value;

    private Username(final String value) {
        this.validateUsername(value);
        this.value = value.trim();
    }

    public static Username of(final String value) {
        return new Username(value);
    }

    public String getValue() {
        return this.value;
    }

    private void validateUsername(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        final String trimmed = value.trim();
        
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Username must have at least " + MIN_LENGTH + " characters");
        }
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username cannot exceed " + MAX_LENGTH + " characters");
        }
        
        if (!trimmed.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, dots, underscores and hyphens");
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
        final Username username = (Username) obj;
        return Objects.equals(this.value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return "Username{" +
                "value='" + this.value + '\'' +
                '}';
    }
}
