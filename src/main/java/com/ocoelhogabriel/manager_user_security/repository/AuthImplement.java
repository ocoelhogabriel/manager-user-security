package com.ocoelhogabriel.manager_user_security.repository;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ocoelhogabriel.manager_user_security.model.AuthModel;

public interface AuthImplement extends UserDetailsService {
	public String generateToken(AuthModel authToken) throws IOException;

	public String validateToken(String token);

}
