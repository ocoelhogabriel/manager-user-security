package com.ocoelhogabriel.manager_user_security.application.usecases.user;

import java.util.Objects;

import com.ocoelhogabriel.manager_user_security.application.dtos.user.CreateUserRequest;
import com.ocoelhogabriel.manager_user_security.application.dtos.user.UserResponse;
import com.ocoelhogabriel.manager_user_security.domain.entities.User;
import com.ocoelhogabriel.manager_user_security.domain.repositories.UserRepository;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.security.PasswordHashingService;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Email;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.HashedPassword;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.Username;

/**
 * Use Case para criação de usuários
 * Aplica Single Responsibility Principle (SRP)
 * Aplica Dependency Inversion Principle (DIP)
 * Segue Clean Architecture - Application Layer
 */
public final class CreateUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordHashingService passwordHashingService;
    
    public CreateUserUseCase(final UserRepository userRepository,
                           final PasswordHashingService passwordHashingService) {
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        this.passwordHashingService = Objects.requireNonNull(passwordHashingService, "PasswordHashingService cannot be null");
    }
    
    public UserResponse execute(final CreateUserRequest request) {
        this.validateRequest(request);
        this.validateUserDoesNotExist(request);
        
        final User newUser = this.createUser(request);
        final User savedUser = this.userRepository.save(newUser);
        
        return this.mapToResponse(savedUser);
    }
    
    private void validateRequest(final CreateUserRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Create user request cannot be null");
        }
    }
    
    private void validateUserDoesNotExist(final CreateUserRequest request) {
        final Username username = Username.of(request.username());
        final Email email = Email.of(request.email());
        
        if (this.userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }
        
        if (this.userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }
    }
    
    private User createUser(final CreateUserRequest request) {
        final Username username = Username.of(request.username());
        final Email email = Email.of(request.email());
        final HashedPassword hashedPassword = this.hashPassword(request.password());
        
        return User.create(username, email, hashedPassword);
    }
    
    private HashedPassword hashPassword(final String rawPassword) {
        final String hashed = this.passwordHashingService.hash(rawPassword);
        return HashedPassword.of(hashed);
    }
    
    private UserResponse mapToResponse(final User user) {
        return new UserResponse(
                user.id().getValue(),
                user.username().getValue(),
                user.email().getValue(),
                user.isActive(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
