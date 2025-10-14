package com.ocoelhogabriel.manager_user_security.application.service;

import com.ocoelhogabriel.manager_user_security.application.dto.TokenDetails;
import com.ocoelhogabriel.manager_user_security.domain.entity.User;
import com.ocoelhogabriel.manager_user_security.domain.exception.AuthenticationException;
import com.ocoelhogabriel.manager_user_security.domain.repository.UserRepository;
import com.ocoelhogabriel.manager_user_security.domain.service.AuthenticationService;
import com.ocoelhogabriel.manager_user_security.infrastructure.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenDetails authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = (User) authentication.getPrincipal();
            return jwtService.generateToken(user);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }
}
