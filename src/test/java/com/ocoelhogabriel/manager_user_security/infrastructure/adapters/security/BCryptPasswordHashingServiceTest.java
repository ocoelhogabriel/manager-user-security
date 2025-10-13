package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security;

import org.junit.jupiter.api.Test;

import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security.BCryptPasswordHashingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para BCryptPasswordHashingService
 * Aplica Clean Architecture - Infrastructure Layer Testing
 * Testa implementação concreta do serviço de hash
 */
@DisplayName("BCryptPasswordHashingService Tests")
class BCryptPasswordHashingServiceTest {

    private BCryptPasswordHashingService passwordHashingService;

    @BeforeEach
    void setUp() {
        passwordHashingService = new BCryptPasswordHashingService();
    }

    @Test
    @DisplayName("Should hash password successfully")
    void shouldHashPasswordSuccessfully() {
        // Given
        final String plainPassword = "mySecretPassword123";
        
        // When
        final String hashedPassword = passwordHashingService.hash(plainPassword);
        
        // Then
        assertNotNull(hashedPassword);
        assertNotEquals(plainPassword, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
        assertTrue(hashedPassword.length() >= 60);
    }

    @Test
    @DisplayName("Should verify password successfully when password matches")
    void shouldVerifyPasswordSuccessfullyWhenPasswordMatches() {
        // Given
        final String plainPassword = "testPassword456";
        final String hashedPassword = passwordHashingService.hash(plainPassword);
        
        // When
        final boolean isValid = passwordHashingService.matches(plainPassword, hashedPassword);
        
        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when password does not match")
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        // Given
        final String correctPassword = "correctPassword";
        final String wrongPassword = "wrongPassword";
        final String hashedPassword = passwordHashingService.hash(correctPassword);
        
        // When
        final boolean isValid = passwordHashingService.matches(wrongPassword, hashedPassword);
        
        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate different hashes for same password")
    void shouldGenerateDifferentHashesForSamePassword() {
        // Given
        final String password = "samePassword";
        
        // When
        final String hash1 = passwordHashingService.hash(password);
        final String hash2 = passwordHashingService.hash(password);
        
        // Then
        assertNotEquals(hash1, hash2);
        
        // But both should verify correctly
        assertTrue(passwordHashingService.matches(password, hash1));
        assertTrue(passwordHashingService.matches(password, hash2));
    }

    @Test
    @DisplayName("Should throw exception for empty password")
    void shouldThrowExceptionForEmptyPassword() {
        // Given
        final String emptyPassword = "";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordHashingService.hash(emptyPassword));
    }

    @Test
    @DisplayName("Should handle long password")
    void shouldHandleLongPassword() {
        // Given
        final String longPassword = "a".repeat(1000);
        
        // When
        final String hashedPassword = passwordHashingService.hash(longPassword);
        
        // Then
        assertNotNull(hashedPassword);
        assertTrue(passwordHashingService.matches(longPassword, hashedPassword));
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        final String specialPassword = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~";
        
        // When
        final String hashedPassword = passwordHashingService.hash(specialPassword);
        
        // Then
        assertNotNull(hashedPassword);
        assertTrue(passwordHashingService.matches(specialPassword, hashedPassword));
    }

    @Test
    @DisplayName("Should handle unicode characters in password")
    void shouldHandleUnicodeCharactersInPassword() {
        // Given
        final String unicodePassword = "héllo wørld 你好 🌍";
        
        // When
        final String hashedPassword = passwordHashingService.hash(unicodePassword);
        
        // Then
        assertNotNull(hashedPassword);
        assertTrue(passwordHashingService.matches(unicodePassword, hashedPassword));
    }

    @Test
    @DisplayName("Should throw exception for null password in hash")
    void shouldThrowExceptionForNullPasswordInHash() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordHashingService.hash(null));
    }

    @Test
    @DisplayName("Should throw exception for null password in matches")
    void shouldThrowExceptionForNullPasswordInMatches() {
        // Given
        final String hashedPassword = "$2a$10$CdWCgQQ6uYL0s5G3Av1k/.kRl5c6aUm8FoU6.kDbRMVk8VNxe9Gke";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordHashingService.matches(null, hashedPassword));
    }

    @Test
    @DisplayName("Should throw exception for null hashed password in matches")
    void shouldThrowExceptionForNullHashedPasswordInMatches() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordHashingService.matches("password", null));
    }

    @Test
    @DisplayName("Should throw exception for whitespace password")
    void shouldThrowExceptionForWhitespacePassword() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> passwordHashingService.hash("   "));
    }
}