package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.service.RolePermissionService;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Permission;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionEvaluatorTest {

    @Mock
    private ResourceService resourceService;

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private PermissionEvaluator permissionEvaluator;

    @Mock
    private UrlPathMatcher urlMatcher;

    @Mock
    private Resource urlResource;

    @Test
    @DisplayName("Should throw NullPointerException for null role name")
    public void testCheckPermissionNullRoleName() {
        // Arrange
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn("USER");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            permissionEvaluator.checkPermission(null, urlMatcher, "GET");
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException for null URL matcher")
    public void testCheckPermissionNullUrlMatcher() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            permissionEvaluator.checkPermission("ADMIN", null, "GET");
        });
    }

    @Test
    @DisplayName("Should check GET permission for SEARCH operation")
    public void testCheckPermissionGetSearch() {
        // Arrange
        String roleName = "ADMIN";
        String method = "GET";
        String resourceName = "USER";

        // Use o mock urlResource já criado pelo Mockito
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(urlMatcher.getMessage()).thenReturn("SEARCH");

        // Mock the domain Resource instead of creating a new instance
        com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource =
            new com.ocoelhogabriel.manager_user_security.domain.entity.Resource(1L, resourceName, "User resource", "/api/users", "v1");

        // Create the Permission with the mocked urlResource
        Permission permission = new Permission(1L, "Admin", "Admin permission", urlResource, true, true, true, true, true);

        when(resourceService.findByName(resourceName)).thenReturn(domainResource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(Resource.class)))
            .thenReturn(permission);

        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should check GET permission for LIST operation")
    public void testCheckPermissionGetList() {
        // Arrange
        String roleName = "ADMIN";
        String method = "GET";
        String resourceName = "USER";

        // Create domain resource
        com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource =
            new com.ocoelhogabriel.manager_user_security.domain.entity.Resource(1L, resourceName, "User resource", "/api/users", "v1");

        // Create permission
        Permission permission = new Permission(1L, "Admin", "Admin permission", urlResource, true, true, true, true, true);

        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(urlMatcher.getMessage()).thenReturn("LIST");
        when(resourceService.findByName(resourceName)).thenReturn(domainResource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(com.ocoelhogabriel.manager_user_security.domain.entity.Resource.class)))
            .thenReturn(permission);

        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);

        // Assert
        assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({ "POST,true,false,false,false,false,true", "POST,false,false,false,false,false,false", "PUT,false,true,false,false,false,true",
            "PUT,false,false,false,false,false,false", "DELETE,false,false,true,false,false,true", "DELETE,false,false,false,false,false,false" })
    @DisplayName("Should check various HTTP methods with permissions")
    public void testCheckPermissionVariousMethods(String method, boolean canCreate, boolean canEdit, boolean canDelete, boolean canList,
            boolean canRead, boolean expectedResult) {
        // Arrange
        String roleName = "USER";
        String resourceName = "USER";

        // Create domain resource
        com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource =
            new com.ocoelhogabriel.manager_user_security.domain.entity.Resource(1L, resourceName, "User resource", "/api/users", "v1");

        // Create permission
        Permission permission = new Permission(1L, "User", "User permission", urlResource, canCreate, canRead, canList, canEdit, canDelete);

        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(resourceService.findByName(resourceName)).thenReturn(domainResource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(com.ocoelhogabriel.manager_user_security.domain.entity.Resource.class)))
            .thenReturn(permission);

        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return false for unsupported HTTP method")
    public void testCheckPermissionUnsupportedMethod() {
        // Arrange
        String roleName = "ADMIN";
        String method = "PATCH";
        String resourceName = "USER";

        // Create domain resource
        com.ocoelhogabriel.manager_user_security.domain.entity.Resource domainResource =
            new com.ocoelhogabriel.manager_user_security.domain.entity.Resource(1L, resourceName, "User resource", "/api/users", "v1");

        // Create permission
        Permission permission = new Permission(1L, "Admin", "Admin permission", urlResource, true, true, true, true, true);

        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(resourceService.findByName(resourceName)).thenReturn(domainResource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(com.ocoelhogabriel.manager_user_security.domain.entity.Resource.class)))
            .thenReturn(permission);

        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);

        // Assert
        assertFalse(result);
    }
}