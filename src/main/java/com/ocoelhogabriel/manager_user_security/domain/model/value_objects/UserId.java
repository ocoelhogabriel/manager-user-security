package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;

/**
 * Value Object to represent a User's unique identifier (ID).
 * Wraps a Long, aligning with the database primary key.
 */
public record UserId(Long value) {

    private static final String FIELD_NAME = "id";

    // The constructor is made public to be accessible from other layers (like the controller).
    public UserId(final Long value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }
        if (value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        this.value = value;
    }
}
