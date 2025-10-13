package com.ocoelhogabriel.manager_user_security.interfaces.web.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.entities.AuthModel;
import com.ocoelhogabriel.manager_user_security.domain.services.AuthServInterface;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.ResponseAuthDTO;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.TokenValidationResponseDTO;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogCategory;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autenticacao")
@Tag(name = "Autenticação", description = "API para Controle de Autenticação e gerenciamento de tokens")
public class AuthenticationController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);
    
	private final AuthServInterface userServImpl;

	public AuthenticationController(AuthServInterface userServImpl) {
		this.userServImpl = userServImpl;
	}

	@PostMapping("/v1/auth")
	@Operation(description = "Realizar autenticação de usuário. Recebe credenciais e retorna um token de acesso.")
	public ResponseEntity<ResponseAuthDTO> postAuth(@Valid @RequestBody @NonNull AuthModel auth) throws AuthenticationException, IOException {
	    LogManager.info(LOGGER, LogCategory.AUTHENTICATION, 
	        MessageFormatterUtil.format(MessageConstraints.LOG_AUTH_LOGIN, auth.getLogin()),
	        "username", auth.getLogin());
	        
		return userServImpl.authLogin(auth);
	}

	@GetMapping("/v1/validate")
	@Operation(description = "Validar token de acesso. Verifica se o token é válido e retorna o status.")
	public ResponseEntity<TokenValidationResponseDTO> validateToken(@RequestParam("token") @NonNull String token) {
	    LogManager.info(LOGGER, LogCategory.AUTHENTICATION, MessageConstraints.LOG_AUTH_TOKEN_VALIDATE);
	    
		try {
			return userServImpl.validateAndParseToken(token);
		} catch (JWTVerificationException e) {
		    LogManager.error(LOGGER, LogCategory.AUTHENTICATION, 
		        MessageFormatterUtil.format(MessageConstraints.AUTH_VALIDATION_ERROR, e.getMessage()),
		        e,
		        "errorType", e.getClass().getSimpleName());
		        
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
			        new TokenValidationResponseDTO(false, 0L, MessageConstraints.AUTH_INVALID_TOKEN));
		}
	}

	@GetMapping("/v1/refresh")
	@Operation(description = "Gerar novo token. Verifica a validade do token e, se expirado, gera um novo token.")
	public ResponseEntity<ResponseAuthDTO> refreshToken(@RequestParam("token") String token) {
	    LogManager.info(LOGGER, LogCategory.AUTHENTICATION, 
	        MessageConstraints.LOG_AUTH_TOKEN_REFRESH,
	        "action", "refreshToken");
	        
		return userServImpl.refreshToken(token);
	}
}
