package com.ocoelhogabriel.manager_user_security.infrastructure.adapters.repositories;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.manager_user_security.domain.entities.AuthModel;

public interface AuthImplement extends UserDetailsService {
	public String generateToken(AuthModel authToken) throws IOException;

	public String validateToken(String token);

}
