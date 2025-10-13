package com.ocoelhogabriel.manager_user_security.application.usecases.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ocoelhogabriel.manager_user_security.application.dtos.user.CreateUserRequest;
import com.ocoelhogabriel.manager_user_security.application.dtos.user.UserResponse;
import com.ocoelhogabriel.manager_user_security.application.usecases.user.CreateUserUseCase;
import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.UserId;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security.PasswordHashingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CreateUserUseCase
 * Aplica Clean Architecture - Application Layer Testing
 * Usa Mockito para isolar dependências
 */
@DisplayName("CreateUserUseCase Tests")
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordHashingService passwordHashingService;
    
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCase(userRepository, passwordHashingService);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "john_doe",
            "john@example.com", 
            "plainPassword123"
        );
        
        final String hashedPassword = "$2a$10$CdWCgQQ6uYL0s5G3Av1k/.kRl5c6aUm8FoU6.kDbRMVk8VNxe9Gke";
        final UserId savedUserId = UserId.of(1L);
        
        when(userRepository.existsByUsername(any(Username.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordHashingService.hash("plainPassword123")).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.restore(
                savedUserId,
                user.username(),
                user.email(),
                user.password(),
                user.isActive(),
                user.createdAt(),
                user.updatedAt()
            );
        });
        
        // When
        final UserResponse response = createUserUseCase.execute(request);
        
        // Then
        assertNotNull(response);
        assertEquals(savedUserId.getValue(), response.id());
        assertEquals("john_doe", response.username());
        assertEquals("john@example.com", response.email());
        assertTrue(response.active());
        assertNotNull(response.createdAt());
        
        // Verify interactions
        verify(userRepository).existsByUsername(Username.of("john_doe"));
        verify(userRepository).existsByEmail(Email.of("john@example.com"));
        verify(passwordHashingService).hash("plainPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "existing_user",
            "user@example.com",
            "password123"
        );
        
        when(userRepository.existsByUsername(any(Username.class))).thenReturn(true);
        
        // When & Then
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createUserUseCase.execute(request)
        );
        
        assertEquals("Username already exists: existing_user", exception.getMessage());
        
        // Verify interactions
        verify(userRepository).existsByUsername(Username.of("existing_user"));
        verify(userRepository, never()).existsByEmail(any());
        verify(passwordHashingService, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "new_user",
            "existing@example.com",
            "password123"
        );
        
        when(userRepository.existsByUsername(any(Username.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);
        
        // When & Then
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createUserUseCase.execute(request)
        );
        
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        
        // Verify interactions
        verify(userRepository).existsByUsername(Username.of("new_user"));
        verify(userRepository).existsByEmail(Email.of("existing@example.com"));
        verify(passwordHashingService, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> createUserUseCase.execute(null));
        
        // Verify no interactions
        verifyNoInteractions(userRepository, passwordHashingService);
    }

    @Test
    @DisplayName("Should validate username format")
    void shouldValidateUsernameFormat() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "ab", // muito curto
            "user@example.com",
            "password123"
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> createUserUseCase.execute(request));
        
        // Verify no repository interactions
        verifyNoInteractions(userRepository, passwordHashingService);
    }

    @Test
    @DisplayName("Should validate email format")
    void shouldValidateEmailFormat() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "valid_user",
            "invalid-email", // formato inválido
            "password123"
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> createUserUseCase.execute(request));
        
        // Verify no repository interactions
        verifyNoInteractions(userRepository, passwordHashingService);
    }

    @Test
    @DisplayName("Should handle repository save failure")
    void shouldHandleRepositorySaveFailure() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "test_user",
            "test@example.com",
            "password123"
        );
        
        when(userRepository.existsByUsername(any(Username.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordHashingService.hash(any())).thenReturn("$2a$10$CdWCgQQ6uYL0s5G3Av1k/.kRl5c6aUm8FoU6.kDbRMVk8VNxe9Gke");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        
        // When & Then
        assertThrows(RuntimeException.class,
                    () -> createUserUseCase.execute(request));
        
        // Verify interactions
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle password hashing failure")
    void shouldHandlePasswordHashingFailure() {
        // Given
        final CreateUserRequest request = new CreateUserRequest(
            "test_user",
            "test@example.com",
            "password123"
        );
        
        when(userRepository.existsByUsername(any(Username.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordHashingService.hash(any())).thenThrow(new RuntimeException("Hashing error"));
        
        // When & Then
        assertThrows(RuntimeException.class,
                    () -> createUserUseCase.execute(request));
        
        // Verify interactions
        verify(passwordHashingService).hash("password123");
        verify(userRepository, never()).save(any());
    }
}