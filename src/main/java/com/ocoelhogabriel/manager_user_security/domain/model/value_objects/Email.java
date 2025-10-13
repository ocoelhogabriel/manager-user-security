package com.ocoelhogabriel.manager_user_security.domain.model.value_objects;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object to represent Email.
 * Utilizes Java 17+ Record for immutability and conciseness.
 * Follows Object Calisthenics - Rule 3: Wrap all primitives and Strings.
 * Applies SRP (Single Responsibility Principle) - responsible only for email representation and validation.
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final String FIELD_NAME = "email";
    private static final int MAX_LENGTH = 100;

    public Email(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }

        final String trimmedValue = value.trim();

        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_MAX_LENGTH, FIELD_NAME, MAX_LENGTH));
        }

        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME));
        }

        this.value = trimmedValue.toLowerCase();
    }
}
