package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.SecurityConfig;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.filter.JwtAuthenticationFilter;

@SpringBootTest
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;
    
    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private HttpSecurity httpSecurity;
    
    @Test
    @DisplayName("Should create password encoder bean")
    public void testPasswordEncoderBean() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("password", passwordEncoder.encode("password")));
    }
    
    @Test
    @DisplayName("Should create authentication manager bean")
    public void testAuthenticationManagerBean() throws Exception {
        // Arrange
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthManager);
        
        // Act
        AuthenticationManager authManager = securityConfig.authenticationManager(authenticationConfiguration);
        
        // Assert
        assertNotNull(authManager);
        assertEquals(mockAuthManager, authManager);
        verify(authenticationConfiguration).getAuthenticationManager();
    }
}