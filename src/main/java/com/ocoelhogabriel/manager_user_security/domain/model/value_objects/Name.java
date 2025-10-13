package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;

/**
 * Value Object to represent a person's name.
 * Ensures that the name is not null, empty, and respects length constraints.
 */
public record Name(String value) {

    private static final String FIELD_NAME = "name";
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    public Name(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }

        final String trimmedValue = value.trim();

        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                "Field '{0}' must be between {1} and {2} characters.", FIELD_NAME, MIN_LENGTH, MAX_LENGTH));
        }

        this.value = trimmedValue;
    }
}
