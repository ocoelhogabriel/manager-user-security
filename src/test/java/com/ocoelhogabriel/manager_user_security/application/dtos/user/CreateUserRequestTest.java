package com.ocoelhogabriel.manager_user_security.application.dtos.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ocoelhogabriel.manager_user_security.application.dtos.user.CreateUserRequest;

import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

/**
 * Testes unitários para CreateUserRequest DTO
 * Aplica Clean Architecture - Application Layer Testing
 * Testa validações e integridade dos dados de entrada
 */
@DisplayName("CreateUserRequest DTO Tests")
class CreateUserRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Should create valid request successfully")
    void shouldCreateValidRequestSuccessfully() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "john_doe",
            "john@example.com",
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("john_doe", request.username());
        assertEquals("john@example.com", request.email());
        assertEquals("password123", request.password());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "  ", "\t", "\n"})
    @DisplayName("Should fail validation for invalid usernames")
    void shouldFailValidationForInvalidUsernames(String username) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            username,
            "john@example.com",
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "username_that_is_way_too_long_to_be_valid"})
    @DisplayName("Should fail validation for username length")
    void shouldFailValidationForUsernameLength(String username) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            username,
            "john@example.com",
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "  ", "invalid-email", "@example.com", "user@", "user"})
    @DisplayName("Should fail validation for invalid emails")
    void shouldFailValidationForInvalidEmails(String email) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "john_doe",
            email,
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "  ", "short"})
    @DisplayName("Should fail validation for invalid passwords")
    void shouldFailValidationForInvalidPasswords(String password) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "john_doe",
            "john@example.com",
            password
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "valid_username",
        "user123",
        "test_user_with_underscores",
        "valid_username_thirty_chars__"
    })
    @DisplayName("Should accept valid usernames")
    void shouldAcceptValidUsernames(String username) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            username,
            "user@example.com",
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user@example.com",
        "test.email@domain.co.uk",
        "user+tag@example.org",
        "firstname.lastname@company.com"
    })
    @DisplayName("Should accept valid emails")
    void shouldAcceptValidEmails(String email) {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "john_doe",
            email,
            "password123"
        );
        
        // When
        final Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should implement record methods correctly")
    void shouldImplementRecordMethodsCorrectly() {
        // Given
        final CreateUserRequest request1 = new CreateUserRequest(
            "john_doe",
            "john@example.com",
            "password123"
        );
        final CreateUserRequest request2 = new CreateUserRequest(
            "john_doe",
            "john@example.com",
            "password123"
        );
        final CreateUserRequest request3 = new CreateUserRequest(
            "jane_doe",
            "jane@example.com",
            "password456"
        );
        
        // Then
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        
        final String expectedToString = "CreateUserRequest[username=john_doe, email=john@example.com, password=password123]";
        assertEquals(expectedToString, request1.toString());
    }
}