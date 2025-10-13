package com.ocoelhogabriel.manager_user_security.unit.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ocoelhogabriel.manager_user_security.application.exception.ApplicationException;
import com.ocoelhogabriel.manager_user_security.application.usecase.AuthenticationUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.AuthenticationException;
import com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.infrastructure.auth.jwt.JwtTokenProvider;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.AuthenticationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.TokenValidationResponse;

@ExtendWith(MockitoExtension.class)
public class AuthenticationUseCaseTest {

    @Mock
    private AuthenticationService authenticationService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private SecurityContext securityContext;
    
    @InjectMocks
    private AuthenticationUseCase authenticationUseCase;
    
    private User testUser;
    private String testToken = "test.jwt.token";
    
    @BeforeEach
    public void setup() {
        testUser = createTestUser();
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Should authenticate user with valid credentials")
    public void testAuthenticateSuccess() {
        // Arrange
        String username = "testuser";
        String password = "password";
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        
        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.generateToken(testUser)).thenReturn(testToken);
        
        Instant expiration = Instant.now().plus(60, ChronoUnit.MINUTES);
        when(jwtTokenProvider.getExpirationFromToken(testToken)).thenReturn(expiration);
        
        // Act
        AuthenticationResponse response = authenticationUseCase.authenticate(username, password);
        
        // Assert
        assertNotNull(response);
        assertEquals(testToken, response.getToken());
        assertEquals(testUser.getId().toString(), response.getUserId());
        assertNotNull(response.getRole());
        assertEquals("ADMIN", response.getRole().getName());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(securityContext).setAuthentication(authentication);
    }
    
    @Test
    @DisplayName("Should throw exception when authentication fails")
    public void testAuthenticateFailure() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationUseCase.authenticate(username, password);
        });
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(userService, jwtTokenProvider);
    }
    
    @Test
    @DisplayName("Should throw exception when user not found")
    public void testAuthenticateUserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        String password = "password";
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        
        when(userService.findByUsername(username)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationUseCase.authenticate(username, password);
        });
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByUsername(username);
        verifyNoInteractions(jwtTokenProvider);
    }
    
    @Test
    @DisplayName("Should validate token successfully")
    public void testValidateTokenSuccess() {
        // Arrange
        String token = testToken;
        
        when(jwtTokenProvider.validateToken(token)).thenReturn("testuser");
        
        Instant expiration = Instant.now().plus(60, ChronoUnit.MINUTES);
        when(jwtTokenProvider.getExpirationFromToken(token)).thenReturn(expiration);
        
        // Act
        TokenValidationResponse response = authenticationUseCase.validateToken(token);
        
        // Assert
        assertNotNull(response);
        assertTrue(response.isValid());
        assertTrue(response.getExpiresIn() > 0);
        assertEquals("Token is valid", response.getMessage());
        
        verify(jwtTokenProvider).validateToken(token);
        verify(jwtTokenProvider).getExpirationFromToken(token);
    }
    
    @Test
    @DisplayName("Should return invalid response when token validation fails")
    public void testValidateTokenFailure() {
        // Arrange
        String token = "invalid.token";
        
        when(jwtTokenProvider.validateToken(token)).thenThrow(new RuntimeException("Invalid token"));
        
        // Act
        TokenValidationResponse response = authenticationUseCase.validateToken(token);
        
        // Assert
        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals(0, response.getExpiresIn());
        assertTrue(response.getMessage().startsWith("Invalid token:"));
        
        verify(jwtTokenProvider).validateToken(token);
    }
    
    @Test
    @DisplayName("Should refresh token successfully")
    public void testRefreshTokenSuccess() {
        // Arrange
        String token = testToken;
        String refreshedToken = "refreshed.jwt.token";
        
        when(jwtTokenProvider.refreshToken(token)).thenReturn(Optional.of(refreshedToken));
        when(jwtTokenProvider.validateToken(refreshedToken)).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        Instant expiration = Instant.now().plus(60, ChronoUnit.MINUTES);
        when(jwtTokenProvider.getExpirationFromToken(refreshedToken)).thenReturn(expiration);
        
        // Act
        AuthenticationResponse response = authenticationUseCase.refreshToken(token);
        
        // Assert
        assertNotNull(response);
        assertEquals(refreshedToken, response.getToken());
        assertTrue(response.getExpiresIn() > 0);
        
        verify(jwtTokenProvider).refreshToken(token);
        verify(jwtTokenProvider).validateToken(refreshedToken);
        verify(userService).findByUsername("testuser");
    }
    
    @Test
    @DisplayName("Should throw exception when token refresh fails")
    public void testRefreshTokenFailure() {
        // Arrange
        String token = "invalid.token";
        
        when(jwtTokenProvider.refreshToken(token)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationUseCase.refreshToken(token);
        });
        
        verify(jwtTokenProvider).refreshToken(token);
    }
    
    @Test
    @DisplayName("Should get current user successfully")
    public void testGetCurrentUserSuccess() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act
        Object result = authenticationUseCase.getCurrentUser();
        
        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verify(userService).findByUsername("testuser");
    }
    
    @Test
    @DisplayName("Should throw exception when not authenticated")
    public void testGetCurrentUserNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationUseCase.getCurrentUser();
        });
        
        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("password");
        
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");
        role.setDescription("Administrator role");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        return user;
    }
}