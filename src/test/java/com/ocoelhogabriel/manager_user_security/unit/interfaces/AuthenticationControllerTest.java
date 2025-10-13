package com.ocoelhogabriel.manager_user_security.unit.interfaces;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ocoelhogabriel.manager_user_security.application.usecase.AuthenticationUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.AuthenticationException;
import com.ocoelhogabriel.manager_user_security.interfaces.api.auth.AuthenticationController;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.AuthenticationRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.AuthenticationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.TokenValidationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.UserRoleDto;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationUseCase authenticationUseCase;
    
    @InjectMocks
    private AuthenticationController authenticationController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Test
    @DisplayName("Should authenticate user and return token")
    public void testAuthenticate() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("testuser", "password");
        
        UserRoleDto role = new UserRoleDto(UUID.randomUUID().toString(), "ADMIN", "Administrator role");
        AuthenticationResponse response = new AuthenticationResponse(
                "jwt.token.here", LocalDateTime.now(), 3600L, UUID.randomUUID().toString(), role);
        
        when(authenticationUseCase.authenticate(anyString(), anyString())).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("jwt.token.here")))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.role.name", is("ADMIN")));
    }
    
    @Test
    @DisplayName("Should return 400 Bad Request for invalid authentication request")
    public void testAuthenticateInvalidRequest() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest(null, "password");
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Should validate token and return validation response")
    public void testValidateToken() throws Exception {
        // Arrange
        TokenValidationResponse response = new TokenValidationResponse(true, 3600L, "Token is valid");
        
        when(authenticationUseCase.validateToken(anyString())).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/v1/validate")
                .param("token", "jwt.token.here"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(true)))
                .andExpect(jsonPath("$.expiresIn", is(3600)))
                .andExpect(jsonPath("$.message", is("Token is valid")));
    }
    
    @Test
    @DisplayName("Should return 401 Unauthorized for invalid token")
    public void testValidateInvalidToken() throws Exception {
        // Arrange
        when(authenticationUseCase.validateToken(anyString())).thenThrow(new AuthenticationException("Invalid token"));
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/v1/validate")
                .param("token", "invalid.token"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Should refresh token and return new token")
    public void testRefreshToken() throws Exception {
        // Arrange
        UserRoleDto role = new UserRoleDto(UUID.randomUUID().toString(), "ADMIN", "Administrator role");
        AuthenticationResponse response = new AuthenticationResponse(
                "new.jwt.token.here", LocalDateTime.now(), 3600L, UUID.randomUUID().toString(), role);
        
        when(authenticationUseCase.refreshToken(anyString())).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/v1/refresh")
                .param("token", "expired.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("new.jwt.token.here")))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.role.name", is("ADMIN")));
    }
    
    @Test
    @DisplayName("Should return 401 Unauthorized for invalid refresh token")
    public void testRefreshInvalidToken() throws Exception {
        // Arrange
        when(authenticationUseCase.refreshToken(anyString())).thenThrow(new AuthenticationException("Invalid token"));
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/v1/refresh")
                .param("token", "invalid.token"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("Should return current user information")
    public void testGetCurrentUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        when(authenticationUseCase.getCurrentUser()).thenReturn(user);
        
        // Act & Assert
        mockMvc.perform(get("/api/auth/v1/me"))
                .andExpect(status().isOk());
    }
}