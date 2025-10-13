package com.ocoelhogabriel.manager_user_security.unit.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.ocoelhogabriel.manager_user_security.application.service.UrlValidationService;

@ExtendWith(MockitoExtension.class)
public class UrlValidationServiceTest {

    @InjectMocks
    private UrlValidationService urlValidationService;
    
    @BeforeEach
    public void setup() {
        // Set up custom public URL patterns
        ReflectionTestUtils.setField(urlValidationService, "publicUrlPatterns", 
                new String[]{"/api/custom/public/.*", "/api/test/open/.*"});
    }
    
    @Test
    @DisplayName("Should identify configured public URLs")
    public void testIsPublicUrlCustomPatterns() {
        // Arrange
        String publicUrl1 = "/api/custom/public/endpoint";
        String publicUrl2 = "/api/test/open/endpoint";
        
        // Act & Assert
        assertTrue(urlValidationService.isPublicUrl(publicUrl1));
        assertTrue(urlValidationService.isPublicUrl(publicUrl2));
    }
    
    @Test
    @DisplayName("Should identify default public URLs")
    public void testIsPublicUrlDefaultPatterns() {
        // Arrange
        String publicUrl1 = "/api/auth/v1/login";
        String publicUrl2 = "/swagger-ui/index.html";
        
        // Act & Assert
        assertTrue(urlValidationService.isPublicUrl(publicUrl1));
        assertTrue(urlValidationService.isPublicUrl(publicUrl2));
    }
    
    @Test
    @DisplayName("Should identify non-public URLs")
    public void testIsPublicUrlNonPublic() {
        // Arrange
        String nonPublicUrl1 = "/api/user/v1/list";
        String nonPublicUrl2 = "/api/role/v1/123";
        
        // Act & Assert
        assertFalse(urlValidationService.isPublicUrl(nonPublicUrl1));
        assertFalse(urlValidationService.isPublicUrl(nonPublicUrl2));
    }
    
    @ParameterizedTest
    @CsvSource({
        "/api/user/v1/list,GET,true",
        "/api/role/v1/create,POST,true",
        "/api/permission/v1/123,GET,true",
        "/invalid/url,GET,false",
        "/api/v1,GET,false",
        "/api/user/version/action,GET,false"
    })
    @DisplayName("Should validate URL format")
    public void testIsValidUrl(String url, String method, boolean expected) {
        // Act
        boolean result = urlValidationService.isValidUrl(url, method);
        
        // Assert
        assertEquals(expected, result);
    }
    
    @Test
    @DisplayName("Should extract resource name from URL")
    public void testExtractResourceNameFromUrl() {
        // Arrange
        String url = "/api/user/v1/list";
        
        // Act
        String resourceName = urlValidationService.extractResourceNameFromUrl(url);
        
        // Assert
        assertEquals("user", resourceName);
    }
    
    @Test
    @DisplayName("Should handle empty resource name")
    public void testExtractResourceNameFromInvalidUrl() {
        // Arrange
        String url = "/api";
        
        // Act
        String resourceName = urlValidationService.extractResourceNameFromUrl(url);
        
        // Assert
        assertEquals("", resourceName);
    }
    
    @ParameterizedTest
    @CsvSource({
        "GET,READ",
        "POST,CREATE",
        "PUT,UPDATE",
        "PATCH,UPDATE",
        "DELETE,DELETE",
        "OPTIONS,UNKNOWN"
    })
    @DisplayName("Should map HTTP methods to actions")
    public void testMapHttpMethodToAction(String method, String expected) {
        // Act
        String action = urlValidationService.mapHttpMethodToAction(method);
        
        // Assert
        assertEquals(expected, action);
    }
    
    @Test
    @DisplayName("Should extract version from URL")
    public void testExtractVersionFromUrl() {
        // Arrange
        String url = "/api/user/v1/list";
        
        // Act
        String version = urlValidationService.extractVersionFromUrl(url);
        
        // Assert
        assertEquals("v1", version);
    }
    
    @Test
    @DisplayName("Should extract action from URL")
    public void testExtractActionFromUrl() {
        // Arrange
        String url = "/api/user/v1/list";
        
        // Act
        String action = urlValidationService.extractActionFromUrl(url);
        
        // Assert
        assertEquals("list", action);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "/api/user/v1/123",
        "/api/role/v1/456",
        "/api/permission/v1/789"
    })
    @DisplayName("Should detect URLs with ID parameters")
    public void testUrlContainsId(String url) {
        // Act & Assert
        assertTrue(urlValidationService.urlContainsId(url));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "/api/user/v1/list",
        "/api/role/v1/create",
        "/api/permission/v1/abc"
    })
    @DisplayName("Should detect URLs without ID parameters")
    public void testUrlDoesNotContainId(String url) {
        // Act & Assert
        assertFalse(urlValidationService.urlContainsId(url));
    }
}