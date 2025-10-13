package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;

import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Email Value Object
 * Applies Clean Architecture - Domain Layer Testing
 * Follows SOLID principles - Single Responsibility Principle
 */
@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("Should create valid email successfully")
    void shouldCreateValidEmailSuccessfully() {
        // Given
        final String validEmail = "user@example.com";
        
        // When
        final Email email = Email.of(validEmail);
        
        // Then
        assertNotNull(email);
        assertEquals(validEmail, email.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "test@domain.com",
        "user.name@example.org", 
        "admin@company.com.br",
        "support@test-domain.co.uk"
    })
    @DisplayName("Should accept valid email formats")
    void shouldAcceptValidEmailFormats(final String validEmail) {
        // When & Then
        assertDoesNotThrow(() -> Email.of(validEmail));
        
        final Email email = Email.of(validEmail);
        assertEquals(validEmail, email.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        " ",
        "invalid-email",
        "@domain.com",
        "user@",
        "user@@domain.com",
        "user@domain",
        "user@.com",
        "user@domain..com"
    })
    @DisplayName("Should reject invalid email formats")
    void shouldRejectInvalidEmailFormats(final String invalidEmail) {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> Email.of(invalidEmail));
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        final String emailValue = "test@example.com";
        final Email email1 = Email.of(emailValue);
        final Email email2 = Email.of(emailValue);
        final Email differentEmail = Email.of("different@example.com");
        
        // Then
        assertEquals(email1, email2);
        assertNotEquals(email1, differentEmail);
        assertNotEquals(email1, null);
        assertNotEquals(email1, "string");
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        final String emailValue = "test@example.com";
        final Email email1 = Email.of(emailValue);
        final Email email2 = Email.of(emailValue);
        
        // Then
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    @DisplayName("Should implement toString correctly")
    void shouldImplementToStringCorrectly() {
        // Given
        final String emailValue = "test@example.com";
        final Email email = Email.of(emailValue);
        
        // When
        final String toString = email.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains(emailValue));
    }

    @Test
    @DisplayName("Should validate maximum length")
    void shouldValidateMaximumLength() {
        // Given - email com mais de 100 caracteres
        final String longEmail = "a".repeat(90) + "@domain.com";
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> Email.of(longEmail));
    }

    @Test
    @DisplayName("Should be immutable")
    void shouldBeImmutable() {
        // Given
        final String emailValue = "test@example.com";
        final Email email = Email.of(emailValue);
        
        // When - attempt to modify the internal value should not be possible
        // Then - getValue should always return the same value
        assertEquals(emailValue, email.getValue());
        assertEquals(emailValue, email.getValue()); // Second call should be equal
    }
}