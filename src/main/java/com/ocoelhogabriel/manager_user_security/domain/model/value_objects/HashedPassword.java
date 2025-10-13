package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;

/**
 * Value Object to represent a hashed password.
 * Ensures the value is not empty and has a format consistent with a BCrypt hash.
 */
public record HashedPassword(String value) {

    private static final String FIELD_NAME = "password";

    public HashedPassword(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }

        // BCrypt hash always starts with $2a$, $2b$, $2x$ or $2y$, has a cost factor, and is 60 chars long in total.
        if (!value.matches("^\\$2[abxy]\\$\\d{2}\\$.{53}$")) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, "BCrypt hash"));
        }
        this.value = value;
    }

    /**
     * Overridden to prevent leaking the hashed password in logs.
     */
    @Override
    public String toString() {
        return "HashedPassword{value='[PROTECTED]'}";
    }
}
