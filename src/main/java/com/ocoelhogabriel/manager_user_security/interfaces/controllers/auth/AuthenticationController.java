package com.ocoelhogabriel.manager_user_security.interfaces.controllers.auth;

import com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.AuthenticationRequest;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.AuthenticationResponse;
import com.ocoelhogabriel.manager_user_security.interfaces.dto.TokenValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for authentication and token management")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/v1/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with username and password, returns an access token"
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(
                request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/validate")
    @Operation(
        summary = "Validate token",
        description = "Validates an access token and returns its status"
    )
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        try {
            TokenValidationResponse response = authenticationService.validateToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(false, 0L, "Invalid token"));
        }
    }

    @GetMapping("/v1/refresh")
    @Operation(
        summary = "Refresh token",
        description = "Refreshes an access token if it's valid"
    )
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestParam String token) {
        try {
            AuthenticationResponse response = authenticationService.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/v1/me")
    @Operation(
        summary = "Get current user",
        description = "Returns information about the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Object> getCurrentUser() {
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }
}