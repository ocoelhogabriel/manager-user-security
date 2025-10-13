package com.ocoelhogabriel.manager_user_security.application.dtos.user;

import org.junit.jupiter.api.Test;

import com.ocoelhogabriel.manager_user_security.application.dtos.user.UserResponse;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Testes unitários para UserResponse DTO
 * Aplica Clean Architecture - Application Layer Testing
 * Testa integridade dos dados de resposta
 */
@DisplayName("UserResponse DTO Tests")
class UserResponseTest {

    @Test
    @DisplayName("Should create response successfully")
    void shouldCreateResponseSuccessfully() {
        // Given
        final Long id = 1L;
        final String username = "john_doe";
        final String email = "john@example.com";
        final boolean active = true;
        final LocalDateTime now = LocalDateTime.now();
        
        // When
        final UserResponse response = new UserResponse(
            id, username, email, active, now, now
        );
        
        // Then
        assertEquals(id, response.id());
        assertEquals(username, response.username());
        assertEquals(email, response.email());
        assertEquals(active, response.active());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // When
        final UserResponse response = new UserResponse(
            null, null, null, false, null, null
        );
        
        // Then
        assertNull(response.id());
        assertNull(response.username());
        assertNull(response.email());
        assertFalse(response.active());
        assertNull(response.createdAt());
        assertNull(response.updatedAt());
    }

    @Test
    @DisplayName("Should implement record methods correctly")
    void shouldImplementRecordMethodsCorrectly() {
        // Given
        final LocalDateTime now = LocalDateTime.now();
        final UserResponse response1 = new UserResponse(
            1L, "john_doe", "john@example.com", true, now, now
        );
        final UserResponse response2 = new UserResponse(
            1L, "john_doe", "john@example.com", true, now, now
        );
        final UserResponse response3 = new UserResponse(
            2L, "jane_doe", "jane@example.com", false, now, now
        );
        
        // Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
        
        final String expectedToString = String.format(
            "UserResponse[id=1, username=john_doe, email=john@example.com, active=true, createdAt=%s, updatedAt=%s]",
            now, now
        );
        assertEquals(expectedToString, response1.toString());
    }

    @Test
    @DisplayName("Should handle different active states")
    void shouldHandleDifferentActiveStates() {
        // Given
        final LocalDateTime now = LocalDateTime.now();
        
        // When
        final UserResponse activeUser = new UserResponse(
            1L, "active_user", "active@example.com", true, now, now
        );
        final UserResponse inactiveUser = new UserResponse(
            2L, "inactive_user", "inactive@example.com", false, now, now
        );
        
        // Then
        assertTrue(activeUser.active());
        assertFalse(inactiveUser.active());
        assertNotEquals(activeUser, inactiveUser);
    }

    @Test
    @DisplayName("Should handle different timestamps")
    void shouldHandleDifferentTimestamps() {
        // Given
        final LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        final LocalDateTime updatedAt = LocalDateTime.now();
        
        // When
        final UserResponse response = new UserResponse(
            1L, "test_user", "test@example.com", true, createdAt, updatedAt
        );
        
        // Then
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
        assertTrue(response.updatedAt().isAfter(response.createdAt()));
    }
}