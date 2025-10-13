package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import java.util.Objects;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;

/**
 * Value Object to represent Email
 * Follows Object Calisthenics - Rule 3: Wrap all primitives and Strings
 * Applies SRP (Single Responsibility Principle) - responsible only for email validation
 */
public final class Email {
    private static final Logger LOGGER = LoggerFactory.getLogger(Email.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final String FIELD_NAME = "email";
    private static final int MAX_LENGTH = 100;
    
    private final String value;

    private Email(final String value) {
        this.validateEmail(value);
        this.value = value.toLowerCase().trim();
    }

    public static Email of(final String value) {
        return new Email(value);
    }

    public String getValue() {
        return this.value;
    }

    private void validateEmail(final String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME);
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_REQUIRED_FIELD, FIELD_NAME));
        }
        
        final String trimmedValue = value.trim();
        
        if (trimmedValue.length() > MAX_LENGTH) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_MAX_LENGTH, FIELD_NAME, MAX_LENGTH);
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_MAX_LENGTH, FIELD_NAME, MAX_LENGTH));
        }
        
        // Specific validations for invalid emails
        if (trimmedValue.startsWith("@") || trimmedValue.endsWith("@") || 
            trimmedValue.contains("@@") || trimmedValue.endsWith(".") ||
            trimmedValue.contains("..")) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME);
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME));
        }
        
        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME);
            }
            throw new IllegalArgumentException(MessageConstraints.MessageFormatter.format(
                MessageConstraints.VALIDATION_INVALID_FORMAT, FIELD_NAME));
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
        final Email email = (Email) obj;
        return Objects.equals(this.value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + this.value + '\'' +
                '}';
    }
}
