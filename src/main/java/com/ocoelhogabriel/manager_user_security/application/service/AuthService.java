package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.domain.model.User;
import com.ocoelhogabriel.manager_user_security.domain.model.value_objects.Username;
import com.ocoelhogabriel.manager_user_security.domain.ports.in.AuthUseCase;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.JwtService;
import com.ocoelhogabriel.manager_user_security.domain.ports.out.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application Service implementing the AuthUseCase input port.
 * This class orchestrates the authentication logic.
 */
@Service
public class AuthService implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
        final AuthenticationManager authenticationManager,
        final UserRepository userRepository,
        final JwtService jwtService
    ) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.jwtService = Objects.requireNonNull(jwtService);
    }

    @Override
    public LoginResult login(LoginCommand command) {
        try {
            // Use Spring Security to perform the authentication
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(command.username(), command.password())
            );

            // If authentication is successful, fetch our domain user
            final User user = userRepository.findByUsername(new Username(command.username()))
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in repository"));

            // Generate the token
            final String token = jwtService.generateToken(user);

            return new LoginResult(token);

        } catch (AuthenticationException e) {
            // Translate Spring's exception to our domain exception
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }
}
