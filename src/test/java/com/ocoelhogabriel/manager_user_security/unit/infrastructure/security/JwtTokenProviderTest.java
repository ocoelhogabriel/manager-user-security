package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.infrastructure.auth.jwt.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    
    @BeforeEach
    public void setup() {
        // Setting necessary properties using reflection
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "test-secret-key-for-junit-testing-which-is-long-enough");
        ReflectionTestUtils.setField(jwtTokenProvider, "expirationMinutes", 60L);
    }
    
    @Test
    @DisplayName("Should generate valid token for a user")
    public void testGenerateToken() {
        // Arrange
        User user = createTestUser();
        
        // Act
        String token = jwtTokenProvider.generateToken(user);
        
        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }
    
    @Test
    @DisplayName("Should validate a token and return username")
    public void testValidateToken() {
        // Arrange
        User user = createTestUser();
        String token = jwtTokenProvider.generateToken(user);
        
        // Act
        String username = jwtTokenProvider.validateToken(token);
        
        // Assert
        assertEquals(user.getUsername(), username);
    }
    
    @Test
    @DisplayName("Should throw exception for invalid token")
    public void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.string";
        
        // Act & Assert
        assertThrows(JWTVerificationException.class, () -> {
            jwtTokenProvider.validateToken(invalidToken);
        });
    }
    
    @Test
    @DisplayName("Should get expiration from token")
    public void testGetExpirationFromToken() {
        // Arrange
        User user = createTestUser();
        String token = jwtTokenProvider.generateToken(user);
        
        // Act
        Instant expiration = jwtTokenProvider.getExpirationFromToken(token);
        
        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.isAfter(Instant.now()));
    }
    
    @Test
    @DisplayName("Should get roles from token")
    public void testGetRolesFromToken() {
        // Arrange
        User user = createTestUser();
        String token = jwtTokenProvider.generateToken(user);
        
        // Act
        String roles = jwtTokenProvider.getRolesFromToken(token);
        
        // Assert
        assertNotNull(roles);
        assertEquals("ADMIN", roles);
    }
    
    @Test
    @DisplayName("Should get user ID from token")
    public void testGetUserIdFromToken() {
        // Arrange
        User user = createTestUser();
        String token = jwtTokenProvider.generateToken(user);
        
        // Act
        Optional<String> userId = jwtTokenProvider.getUserIdFromToken(token);
        
        // Assert
        assertTrue(userId.isPresent());
        assertEquals(user.getId().toString(), userId.get());
    }
    
    @Test
    @DisplayName("Should refresh valid token")
    public void testRefreshToken() {
        // Arrange
        User user = createTestUser();
        String token = jwtTokenProvider.generateToken(user);
        
        // Act
        Optional<String> refreshedToken = jwtTokenProvider.refreshToken(token);
        
        // Assert
        assertTrue(refreshedToken.isPresent());
        assertNotEquals(token, refreshedToken.get());
    }
    
    @Test
    @DisplayName("Should return empty for invalid refresh token")
    public void testRefreshInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.string";
        
        // Act
        Optional<String> refreshedToken = jwtTokenProvider.refreshToken(invalidToken);
        
        // Assert
        assertTrue(refreshedToken.isEmpty());
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("password");
        
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        return user;
    }
}