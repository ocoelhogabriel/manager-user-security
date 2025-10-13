package com.ocoelhogabriel.usersecurity.interfaces.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocoelhogabriel.usersecurity.application.usecase.AuthenticationUseCase;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.AuthenticationRequest;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.AuthenticationResponse;
import com.ocoelhogabriel.usersecurity.interfaces.api.dto.TokenValidationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for authentication operations.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for authentication and token management")
public class AuthenticationController {

    @Autowired
    private AuthenticationUseCase authenticationUseCase;

    /**
     * Authenticates a user with username and password.
     *
     * @param request the authentication request
     * @return the authentication response with the JWT token
     */
    @PostMapping("/v1/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with username and password, returns an access token"
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationUseCase.authenticate(
                request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Validates a JWT token.
     *
     * @param token the token to validate
     * @return the validation response
     */
    @GetMapping("/v1/validate")
    @Operation(
        summary = "Validate token",
        description = "Validates an access token and returns its status"
    )
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        try {
            TokenValidationResponse response = authenticationUseCase.validateToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(false, 0L, "Invalid token"));
        }
    }

    /**
     * Refreshes a JWT token.
     *
     * @param token the token to refresh
     * @return the authentication response with the new token
     */
    @GetMapping("/v1/refresh")
    @Operation(
        summary = "Refresh token",
        description = "Refreshes an access token if it's valid"
    )
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestParam String token) {
        try {
            AuthenticationResponse response = authenticationUseCase.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Gets the current authenticated user information.
     *
     * @return the current user information
     */
    @GetMapping("/v1/me")
    @Operation(
        summary = "Get current user",
        description = "Returns information about the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Object> getCurrentUser() {
        return ResponseEntity.ok(authenticationUseCase.getCurrentUser());
    }
}