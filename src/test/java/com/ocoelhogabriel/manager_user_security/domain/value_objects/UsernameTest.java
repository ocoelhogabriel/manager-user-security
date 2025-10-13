package com.ocoelhogabriel.manager_user_security.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Username Value Object
 * Applies Clean Architecture - Domain Layer Testing
 * Follows SOLID principles - Single Responsibility Principle
 */
@DisplayName("Username Value Object Tests")
class UsernameTest {

    @Test
    @DisplayName("Should create valid username successfully")
    void shouldCreateValidUsernameSuccessfully() {
        // Given
        final String validUsername = "john_doe";
        
        // When
        final Username username = Username.of(validUsername);
        
        // Then
        assertNotNull(username);
        assertEquals(validUsername, username.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user123",
        "admin",
        "john_doe",
        "test-user",
        "User123",
        "a1b2c3"
    })
    @DisplayName("Should accept valid username formats")
    void shouldAcceptValidUsernameFormats(final String validUsername) {
        // When & Then
        assertDoesNotThrow(() -> Username.of(validUsername));
        
        final Username username = Username.of(validUsername);
        assertEquals(validUsername, username.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        " ",
        "  ",
        "ab", // muito curto
        "user@domain", // caractere inválido @
        "user#123", // caractere inválido #
        "user with spaces"
    })
    @DisplayName("Should reject invalid username formats")
    void shouldRejectInvalidUsernameFormats(final String invalidUsername) {
        // When & Then
        final Exception exception = assertThrows(IllegalArgumentException.class, 
                    () -> Username.of(invalidUsername));
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should validate minimum length")
    void shouldValidateMinimumLength() {
        // Given
        final String shortUsername = "ab"; // 2 caracteres
        
        // When & Then
        final Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> Username.of(shortUsername));
        assertTrue(exception.getMessage().contains("3"));
    }

    @Test
    @DisplayName("Should validate maximum length")
    void shouldValidateMaximumLength() {
        // Given - username com mais de 50 caracteres
        final String longUsername = "a".repeat(51);
        
        // When & Then
        final Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> Username.of(longUsername));
        assertTrue(exception.getMessage().contains("50"));
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        final String usernameValue = "testuser";
        final Username username1 = Username.of(usernameValue);
        final Username username2 = Username.of(usernameValue);
        final Username differentUsername = Username.of("different");
        
        // Then
        assertEquals(username1, username2);
        assertNotEquals(username1, differentUsername);
        assertNotEquals(username1, null);
        assertNotEquals(username1, "string");
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        final String usernameValue = "testuser";
        final Username username1 = Username.of(usernameValue);
        final Username username2 = Username.of(usernameValue);
        
        // Then
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    @DisplayName("Should implement toString correctly")
    void shouldImplementToStringCorrectly() {
        // Given
        final String usernameValue = "testuser";
        final Username username = Username.of(usernameValue);
        
        // When
        final String toString = username.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains(usernameValue));
    }

    @Test
    @DisplayName("Should be case sensitive")
    void shouldBeCaseSensitive() {
        // Given
        final Username username1 = Username.of("TestUser");
        final Username username2 = Username.of("testuser");
        
        // Then
        assertNotEquals(username1, username2);
    }

    @Test
    @DisplayName("Should be immutable")
    void shouldBeImmutable() {
        // Given
        final String usernameValue = "testuser";
        final Username username = Username.of(usernameValue);
        
        // When - attempt to modify the internal value should not be possible
        // Then - getValue should always return the same value
        assertEquals(usernameValue, username.getValue());
        assertEquals(usernameValue, username.getValue()); // Segunda chamada deve ser igual
    }
}