package com.ocoelhogabriel.manager_user_security.infrastructure.web;

import com.ocoelhogabriel.manager_user_security.domain.ports.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Driving Adapter (Controller) for authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API for Authentication and Token Management")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(final AuthUseCase authUseCase) {
        this.authUseCase = Objects.requireNonNull(authUseCase);
    }

    // --- DTOs for the API Layer ---
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token) {}

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and receive a JWT")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest request) {
        final var command = new AuthUseCase.LoginCommand(request.username(), request.password());
        final AuthUseCase.LoginResult result = authUseCase.login(command);
        return ResponseEntity.ok(new LoginResponse(result.token()));
    }
}
