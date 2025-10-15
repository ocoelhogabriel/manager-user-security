package com.ocoelhogabriel.manager_user_security.interfaces.controllers;

import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.DomainException;
import com.ocoelhogabriel.manager_user_security.domain.service.UserService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.CreateUserRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.UpdatePasswordRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.UpdateUserRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.UserResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for user management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    @PostMapping("/v1")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create user", description = "Creates a new user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toResponse(user));
    }

    /**
     * Gets a user by ID.
     *
     * @param id the user ID
     * @return the user
     */
    @GetMapping("/v1/{id}")
    @Operation(summary = "Get user", description = "Gets a user by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
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
                .map(userMapper::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Updates a user.
     *
     * @param id      the user ID
     * @param request the user update request
     * @return the updated user
     */
    @PutMapping("/v1/{id}")
    @Operation(summary = "Update user", description = "Updates a user")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        return userService.findById(id)
                .map(user -> {
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    User updatedUser = userService.updateUser(user);
                    return ResponseEntity.ok(userMapper.toResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a user's password.
     *
     * @param id      the user ID
     * @param request the password update request
     * @return the updated user
     */
    @PutMapping("/v1/{id}/password")
    @Operation(summary = "Update password", description = "Updates a user's password")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequest request) {

        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.updatePassword(user, request.getPassword());
                    return ResponseEntity.ok(userMapper.toResponse(updatedUser));
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
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.activateUser(user);
                    return ResponseEntity.ok(userMapper.toResponse(updatedUser));
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
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> {
                    User updatedUser = userService.deactivateUser(user);
                    return ResponseEntity.ok(userMapper.toResponse(updatedUser));
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (DomainException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
