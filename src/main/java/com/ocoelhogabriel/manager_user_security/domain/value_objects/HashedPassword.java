package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;

/**
 * Value Object para representar senha criptografada
 * Segue Object Calisthenics - Regra 3: Wrap all primitives and Strings
 * Aplica SRP - responsável apenas por validação de senha
 */
public final class HashedPassword {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashedPassword.class);
    private final String value;

    private HashedPassword(final String value) {
        this.validatePassword(value);
        this.value = value;
    }

    public static HashedPassword of(final String hashedValue) {
        return new HashedPassword(hashedValue);
    }

    public String getValue() {
        return this.value;
    }

    private void validatePassword(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, "password");
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, "password"));
        }
        
        // BCrypt hash sempre começa com $2a$, $2b$, $2x$ ou $2y$ e tem 60 caracteres
        if (!value.matches("^\\$2[abxy]\\$\\d{2}\\$.{53}$")) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, "BCrypt hash");
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, "BCrypt hash"));
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
        final HashedPassword that = (HashedPassword) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return "HashedPassword{value='[PROTECTED]'}";
    }
}
