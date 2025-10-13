package com.ocoelhogabriel.manager_user_security.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.infrastructure.adapters.persistence.entities.Usuario;
import com.ocoelhogabriel.manager_user_security.interfaces.web.dtos.GenerateTokenRecords;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogCategory;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogManager;

@Component
public class JWTUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(JWTUtil.class);
	private static final DateTimeFormatter dtfEditado = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final String JWT_ISSUER = "auth-api";

	@Value("${api.security.token.secret}")
	private String secret;

	@Value("${api.security.expiration.time.minutes}")
	private long expirationTime;
	
	public JWTUtil() {
		// Construtor padrão
	}

	public String getSecret() {
		return secret;
	}

	public GenerateTokenRecords generateToken(Usuario usuario) {
		try {
			LogManager.debug(LOGGER, LogCategory.SECURITY, 
				MessageFormatterUtil.format(MessageConstraints.LOG_JWT_TOKEN_GENERATION, usuario.getUsername()),
				"username", usuario.getUsername());
				
			Algorithm algorithm = Algorithm.HMAC256(secret);
			Instant expirationInstant = genExpirationDate();
			String token = JWT.create()
				.withIssuer(JWT_ISSUER)
				.withSubject(usuario.getUsername())
				.withExpiresAt(Date.from(expirationInstant))
				.withClaim("role", usuario.getPerfil().getNome())
				.sign(algorithm);

			LogManager.debug(LOGGER, LogCategory.SECURITY, 
				MessageConstraints.LOG_JWT_TOKEN_GENERATED_SUCCESSFULLY,
				"username", usuario.getUsername());
			
			return new GenerateTokenRecords(
				usuario.getUsername(), 
				token, 
				Utils.newDateString(), 
				dtfEditado.format(expirationInstant.atZone(ZoneId.systemDefault()))
			);
		} catch (JWTCreationException | IllegalArgumentException e) {
			LogManager.error(LOGGER, LogCategory.SECURITY, 
				MessageFormatterUtil.format(MessageConstraints.ERROR_JWT_TOKEN_GENERATION, e.getMessage()), 
				e,
				"errorType", e.getClass().getSimpleName());
				
			throw new JWTCreationException(MessageConstraints.ERROR_JWT_TOKEN_GENERATION, e);
		}
	}

	public String validateToken(String token) {
		LogManager.debug(LOGGER, LogCategory.SECURITY, MessageConstraints.LOG_JWT_TOKEN_VALIDATION);
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
			String subject = verifier.verify(token).getSubject();
			
			LogManager.debug(LOGGER, LogCategory.SECURITY, 
				MessageConstraints.LOG_JWT_TOKEN_VALIDATION_SUCCESS,
				"subject", subject);
			
			return subject;
		} catch (TokenExpiredException e) {
			LogManager.warn(LOGGER, LogCategory.SECURITY, 
				MessageConstraints.SECURITY_TOKEN_EXPIRED, 
				e,
				"tokenStatus", "expired");
				
			throw new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_EXPIRED, e);
		} catch (JWTVerificationException e) {
			LogManager.error(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.LOG_JWT_TOKEN_VALIDATION_FAILED, e.getMessage()),
				e,
				"errorType", e.getClass().getSimpleName());
				
			throw new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_INVALID, e);
		}
	}

	public GenerateTokenRecords validateOrRefreshToken(String token) {
		LogManager.debug(LOGGER, LogCategory.SECURITY, MessageConstraints.LOG_JWT_TOKEN_VALIDATION);
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
			DecodedJWT jwt = verifier.verify(token);
			
			LogManager.debug(LOGGER, LogCategory.SECURITY, 
				MessageConstraints.LOG_JWT_TOKEN_VALIDATION_SUCCESS,
				"subject", jwt.getSubject());

			return new GenerateTokenRecords(
				jwt.getSubject(), 
				token, 
				Utils.newDateString(), 
				dtfEditado.format(jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()))
			);
		} catch (TokenExpiredException exception) {
			LogManager.info(LOGGER, LogCategory.SECURITY,
				MessageConstraints.SECURITY_TOKEN_EXPIRED + " Tentando atualizar.",
				"action", "refreshToken");
				
			return refreshToken(token);
		} catch (JWTVerificationException exception) {
			LogManager.error(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.ERROR_JWT_TOKEN_VALIDATION, exception.getMessage()),
				exception,
				"errorType", exception.getClass().getSimpleName());
				
			throw new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_INVALID);
		}
	}

	public GenerateTokenRecords refreshToken(String refreshToken) {
		LogManager.debug(LOGGER, LogCategory.SECURITY, MessageConstraints.LOG_JWT_TOKEN_REFRESH);
		
		try {
			Algorithm refreshAlgorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(refreshAlgorithm).withIssuer(JWT_ISSUER).build();
			DecodedJWT decodedToken = verifier.verify(refreshToken);

			String username = decodedToken.getSubject();
			LogManager.debug(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.LOG_JWT_TOKEN_GENERATION, username),
				"username", username);
			
			Usuario user = new Usuario();
			user.setUsername(username);
			
			GenerateTokenRecords newToken = generateToken(user);
			LogManager.info(LOGGER, LogCategory.SECURITY,
				MessageConstraints.LOG_JWT_TOKEN_REFRESH_SUCCESS,
				"username", username);
			
			return newToken;
		} catch (TokenExpiredException e) {
			LogManager.warn(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.LOG_JWT_TOKEN_REFRESH_FAILED, "Token expirado"),
				e,
				"reason", "expired");
				
			throw new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_EXPIRED);
		} catch (JWTVerificationException e) {
			LogManager.error(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.ERROR_JWT_TOKEN_REFRESH, e.getMessage()),
				e,
				"errorType", e.getClass().getSimpleName());
				
			throw new AccessDeniedException(MessageConstraints.SECURITY_TOKEN_INVALID);
		}
	}

	private Instant genExpirationDate() {
		return Instant.now().plusSeconds(expirationTime * 60);
	}

	public Instant getExpirationDateFromToken(String token) {
		LogManager.debug(LOGGER, LogCategory.SECURITY, "Obtendo data de expiração do token");
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
			DecodedJWT jwt = verifier.verify(token);
			
			Instant expirationDate = jwt.getExpiresAt().toInstant();
			String formattedDate = dtfEditado.format(expirationDate.atZone(ZoneId.systemDefault()));
			
			LogManager.debug(LOGGER, LogCategory.SECURITY, 
				"Data de expiração do token obtida", 
				"expirationDate", formattedDate,
				"subject", jwt.getSubject());
			
			return expirationDate;
		} catch (JWTVerificationException exception) {
			LogManager.error(LOGGER, LogCategory.SECURITY,
				MessageFormatterUtil.format(MessageConstraints.ERROR_JWT_TOKEN_VALIDATION, exception.getMessage()),
				exception,
				"errorType", exception.getClass().getSimpleName());
				
			throw new JWTVerificationException(MessageFormatterUtil.format(MessageConstraints.ERROR_JWT_TOKEN_VALIDATION, exception.getMessage()));
		}
	}

	public static String convertDateToString(Date date) {
		Objects.requireNonNull(date, "A Data de entrada para conversão de Date para String está nula.");
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return dtfEditado.format(localDateTime);
	}

}
