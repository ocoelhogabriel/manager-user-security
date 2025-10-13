package com.ocoelhogabriel.manager_user_security.unit.domain.value_objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;

/**
 * Unit tests for Email Value Object
 * Follows quality test patterns with AAA (Arrange, Act, Assert)
 * Complete coverage of positive and negative scenarios
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Email Value Object Tests")
class EmailTest {
    
    @Test
    @DisplayName("Should create valid email successfully")
    void shouldCreateValidEmailSuccessfully() {
        // Arrange
        final String validEmailString = "user@example.com";
        
        // Act
        final Email email = Email.of(validEmailString);
        
        // Assert
        assertNotNull(email);
        assertEquals("user@example.com", email.getValue());
    }
    
    @Test
    @DisplayName("Should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
        // Arrange
        final String mixedCaseEmail = "User@EXAMPLE.COM";
        
        // Act
        final Email email = Email.of(mixedCaseEmail);
        
        // Assert
        assertEquals("user@example.com", email.getValue());
    }
    
    @Test
    @DisplayName("Should trim whitespace from email")
    void shouldTrimWhitespaceFromEmail() {
        // Arrange
        final String emailWithWhitespace = "  user@example.com  ";
        
        // Act
        final Email email = Email.of(emailWithWhitespace);
        
        // Assert
        assertEquals("user@example.com", email.getValue());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "invalid-email",
        "@example.com",
        "user@",
        "user.example.com",
        "user@.com",
        "user@com",
        "user@example.",
        "user name@example.com"
    })
    @DisplayName("Should throw exception for invalid emails")
    void shouldThrowExceptionForInvalidEmails(final String invalidEmail) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> Email.of(invalidEmail));
        assertNotNull(exception);
    }
    
    @Test
    @DisplayName("Should throw exception for null email")
    void shouldThrowExceptionForNullEmail() {
        // Act & Assert
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(null)
        );
        
        assertEquals("Email field is required.", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should be equal when emails are the same")
    void shouldBeEqualWhenEmailsAreTheSame() {
        // Arrange
        final Email email1 = Email.of("user@example.com");
        final Email email2 = Email.of("USER@EXAMPLE.COM");
        
        // Act & Assert
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }
    
    @Test
    @DisplayName("Should not be equal when emails are different")
    void shouldNotBeEqualWhenEmailsAreDifferent() {
        // Arrange
        final Email email1 = Email.of("user1@example.com");
        final Email email2 = Email.of("user2@example.com");
        
        // Act & Assert
        assertNotEquals(email1, email2);
    }
    
    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Arrange
        final Email email = Email.of("user@example.com");
        
        // Act & Assert
        assertNotEquals(null, email);
    }
    
    @Test
    @DisplayName("Should not be equal to different type")
    void shouldNotBeEqualToDifferentType() {
        // Arrange
        final Email email = Email.of("user@example.com");
        final String string = "user@example.com";
        
        // Act & Assert
        assertNotEquals(string, email);
    }
    
    @Test
    @DisplayName("Should have meaningful toString representation")
    void shouldHaveMeaningfulToStringRepresentation() {
        // Arrange
        final Email email = Email.of("user@example.com");
        
        // Act
        final String toString = email.toString();
        
        // Assert
        assertTrue(toString.contains("Email"));
        assertTrue(toString.contains("user@example.com"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@example.co.uk",
        "user+tag@example.org",
        "123@numeric.com",
        "user_name@test-domain.com"
    })
    @DisplayName("Should accept various valid email formats")
    void shouldAcceptVariousValidEmailFormats(final String validEmail) {
        // Act & Assert
        assertDoesNotThrow(() -> Email.of(validEmail));
        
        final Email email = Email.of(validEmail);
        assertNotNull(email);
        assertEquals(validEmail.toLowerCase().trim(), email.getValue());
    }
}