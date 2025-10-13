package com.ocoelhogabriel.manager_user_security.interfaces.api.resource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.CreateUserRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.UpdatePasswordRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.UpdateUserRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.api.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for user management.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for user management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;
    
    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    @PostMapping("/v1")
    @Operation(summary = "Create user", description = "Creates a new user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(
                request.getUsername(), 
                request.getEmail(), 
                request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapUserToResponse(user));
    }
    
    /**
     * Gets a user by ID.
     *
     * @param id the user ID
     * @return the user
     */
    @GetMapping("/v1/{id}")
    @Operation(summary = "Get user", description = "Gets a user by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(mapUserToResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Gets all users.
     *
     * @return all users
     */
    @GetMapping("/v1")
    @Operation(summary = "Get all users", description = "Gets all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    
    /**
     * Updates a user.
     *
     * @param id the user ID
     * @param request the user update request
     * @return the updated user
     */
    @PutMapping("/v1/{id}")
    @Operation(summary = "Update user", description = "Updates a user")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id, 
            @Valid @RequestBody UpdateUserRequest request) {
        
        return userService.findById(id)
                .map(user -> {
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    User updatedUser = userService.updateUser(user);
                    return ResponseEntity.ok(mapUserToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Updates a user's password.
     *
     * @param id the user ID
     * @param request the password update request
     * @return the updated user
     */
    @PutMapping("/v1/{id}/password")
    @Operation(summary = "Update password", description = "Updates a user's password")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable UUID id, 
            @Valid @RequestBody UpdatePasswordRequest request) {
        
        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.updatePassword(user, request.getPassword());
                    return ResponseEntity.ok(mapUserToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Activates a user.
     *
     * @param id the user ID
     * @return the updated user
     */
    @PutMapping("/v1/{id}/activate")
    @Operation(summary = "Activate user", description = "Activates a user")
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.activateUser(user);
                    return ResponseEntity.ok(mapUserToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Deactivates a user.
     *
     * @param id the user ID
     * @return the updated user
     */
    @PutMapping("/v1/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivates a user")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.deactivateUser(user);
                    return ResponseEntity.ok(mapUserToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Deletes a user.
     *
     * @param id the user ID
     * @return no content
     */
    @DeleteMapping("/v1/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (DomainException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Maps a User entity to a UserResponse DTO.
     *
     * @param user the user entity
     * @return the user response DTO
     */
    private UserResponse mapUserToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                user.getRoles().stream()
                    .map(role -> new UserResponse.RoleResponse(
                            role.getId(),
                            role.getName(),
                            role.getDescription()))
                    .collect(Collectors.toSet())
        );
    }
}
