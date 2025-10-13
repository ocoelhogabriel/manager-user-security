package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;

/**
 * Value Object to represent an access level.
 * Ensures the level is within a valid range.
 */
public record NivelAcesso(Integer value) {

    private static final String FIELD_NAME = "nivelAcesso";
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 10;

    public NivelAcesso(final Integer value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }
        if (value < MIN_LEVEL || value > MAX_LEVEL) {
            throw new IllegalArgumentException("Access Level must be between " + MIN_LEVEL + " and " + MAX_LEVEL + ".");
        }
        this.value = value;
    }
}
