package com.ocoelhogabriel.manager_user_security.unit.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ocoelhogabriel.manager_user_security.domain.entity.Permission;
import com.ocoelhogabriel.manager_user_security.domain.entity.Resource;
import com.ocoelhogabriel.manager_user_security.domain.entity.Role;
import com.ocoelhogabriel.manager_user_security.domain.service.ResourceService;
import com.ocoelhogabriel.manager_user_security.domain.service.RolePermissionService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.PermissionEvaluator;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.UrlPathMatcher;

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
    private com.ocoelhogabriel.manager_user_security.infrastructure.security.authorization.Resource urlResource;
    
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
        
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName(resourceName);
        
        Permission permission = new Permission();
        permission.setCanRead(true);
        
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(urlMatcher.getMessage()).thenReturn("SEARCH");
        when(resourceService.findByName(resourceName)).thenReturn(resource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(Resource.class))).thenReturn(permission);
        
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
        
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName(resourceName);
        
        Permission permission = new Permission();
        permission.setCanList(true);
        
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(urlMatcher.getMessage()).thenReturn("LIST");
        when(resourceService.findByName(resourceName)).thenReturn(resource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(Resource.class))).thenReturn(permission);
        
        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);
        
        // Assert
        assertTrue(result);
    }
    
    @ParameterizedTest
    @CsvSource({
        "POST,true,false,false,false,false,true",
        "POST,false,false,false,false,false,false",
        "PUT,false,true,false,false,false,true",
        "PUT,false,false,false,false,false,false",
        "DELETE,false,false,true,false,false,true",
        "DELETE,false,false,false,false,false,false"
    })
    @DisplayName("Should check various HTTP methods with permissions")
    public void testCheckPermissionVariousMethods(String method, boolean canCreate, boolean canEdit, 
            boolean canDelete, boolean canList, boolean canRead, boolean expectedResult) {
        // Arrange
        String roleName = "USER";
        String resourceName = "USER";
        
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName(resourceName);
        
        Permission permission = new Permission();
        permission.setCanCreate(canCreate);
        permission.setCanEdit(canEdit);
        permission.setCanDelete(canDelete);
        permission.setCanList(canList);
        permission.setCanRead(canRead);
        
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(resourceService.findByName(resourceName)).thenReturn(resource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(Resource.class))).thenReturn(permission);
        
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
        
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName(resourceName);
        
        Permission permission = new Permission();
        permission.setCanCreate(true);
        permission.setCanEdit(true);
        permission.setCanDelete(true);
        
        when(urlMatcher.getResource()).thenReturn(urlResource);
        when(urlResource.name()).thenReturn(resourceName);
        when(resourceService.findByName(resourceName)).thenReturn(resource);
        when(rolePermissionService.findByRoleAndResource(any(Role.class), any(Resource.class))).thenReturn(permission);
        
        // Act
        boolean result = permissionEvaluator.checkPermission(roleName, urlMatcher, method);
        
        // Assert
        assertFalse(result);
    }
}