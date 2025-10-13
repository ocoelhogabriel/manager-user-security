package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;

public class UrlPathMatcherTest {

    @Test
    @DisplayName("Should validate correct API URL")
    public void testValidateUrlCorrectApiUrl() {
        // Arrange
        String url = "/manager_user_security/api/user/v1/list";
        String method = "GET";
        
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertTrue(result.getMessage().startsWith("Valid URL!"));
    }
    
    @Test
    @DisplayName("Should detect invalid URL format")
    public void testValidateUrlInvalidFormat() {
        // Arrange
        String url = "/invalid/url";
        String method = "GET";
        
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNull(result.getResource());
        assertEquals("Invalid URL format", result.getMessage());
    }
    
    @Test
    @DisplayName("Should recognize URL with ID parameter")
    public void testValidateUrlWithId() {
        // Arrange
        String url = "/manager_user_security/api/user/v1/123";
        String method = "GET";
        
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertEquals("SEARCH", result.getMessage());
    }
    
    @Test
    @DisplayName("Should reject URL with invalid ID parameter")
    public void testValidateUrlWithInvalidId() {
        // Arrange
        String url = "/manager_user_security/api/user/v1/abc";
        String method = "GET";
        
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertTrue(result.getMessage().contains("Invalid URL for SEARCH action"));
    }
    
    @ParameterizedTest
    @CsvSource({
        "/manager_user_security/api/user/v1/list, GET",
        "/manager_user_security/api/role/v1/list, GET",
        "/manager_user_security/api/permission/v1/create, POST",
        "/manager_user_security/api/resource/v1/update, PUT"
    })
    @DisplayName("Should validate various correct URLs")
    public void testValidateUrlVariousCorrectUrls(String url, String method) {
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertTrue(result.getMessage().startsWith("Valid URL!"));
    }
    
    @ParameterizedTest
    @CsvSource({
        "/invalid, GET",
        "/manager_user_security, GET",
        "/manager_user_security/api, GET",
        "/manager_user_security/api/user, GET"
    })
    @DisplayName("Should reject various invalid URLs")
    public void testValidateUrlVariousInvalidUrls(String url, String method) {
        // Act
        UrlPathMatcher result = UrlPathMatcher.validateUrl(url, method);
        
        // Assert
        assertNotNull(result);
        assertNull(result.getResource());
        assertEquals("Invalid URL format", result.getMessage());
    }
}