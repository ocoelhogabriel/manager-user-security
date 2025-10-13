package com.ocoelhogabriel.manager_user_security.exception;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageTemplateKeys;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogCategory;
import com.ocoelhogabriel.manager_user_security.utils.logging.LogManager;
import com.ocoelhogabriel.manager_user_security.utils.message.MessageResponse;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);
	
	// Constantes para chaves de log padronizadas
	private static final String LOG_KEY_EXCEPTION = "exception";
	private static final String LOG_KEY_MESSAGE = "message";
	private static final String LOG_KEY_ERROR_TYPE = "errorType";
	private static final String LOG_KEY_USERNAME = "username";
	private static final String LOG_KEY_ENTITY_TYPE = "entityType";
	private static final String LOG_KEY_ENTITY_ID = "entityId";
	private static final String LOG_KEY_PATH = "path";
	private static final String LOG_KEY_LOCATION = "location";
	private static final String LOG_KEY_EXPIRY = "expiry";
	private static final String LOG_KEY_ERROR_POSITION = "errorPosition";
	
	public GlobalExceptionHandler() {
		// Construtor sem parâmetros
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseGlobalModel> handleAccessDeniedException(AccessDeniedException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put(LOG_KEY_EXCEPTION, "AccessDeniedException");
		logParams.put(LOG_KEY_MESSAGE, ex.getMessage());
		logParams.put(LOG_KEY_ERROR_TYPE, "SECURITY");
		
		LogManager.error(log, LogCategory.SECURITY, "Acesso negado: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(
				MessageConstraints.AUTH_ACCESS_DENIED));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseGlobalModel> handleAuthenticationException(AuthenticationException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put(LOG_KEY_EXCEPTION, "AuthenticationException");
		logParams.put(LOG_KEY_MESSAGE, ex.getMessage());
		logParams.put(LOG_KEY_ERROR_TYPE, "AUTHENTICATION");

		LogManager.error(log, LogCategory.SECURITY, "Falha na autenticação: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(
				MessageConstraints.AUTH_INVALID_CREDENTIALS));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseGlobalModel> handleEntityNotFoundException(EntityNotFoundException ex) {
		// Extrair informação sobre qual entidade não foi encontrada do erro
		String entityType = extractEntityTypeFromMessage(ex.getMessage());
		String errorMessage = MessageFormatterUtil.formatTemplate(entityType + ".not.found.id", 
				extractIdFromMessage(ex.getMessage()));
		
		Map<String, Object> logParams = new HashMap<>();
		logParams.put(LOG_KEY_EXCEPTION, "EntityNotFoundException");
		logParams.put(LOG_KEY_MESSAGE, ex.getMessage());
		logParams.put(LOG_KEY_ENTITY_TYPE, entityType);
		logParams.put(LOG_KEY_ENTITY_ID, extractIdFromMessage(ex.getMessage()));
		logParams.put(LOG_KEY_ERROR_TYPE, "DATA");

		LogManager.error(log, LogCategory.DATABASE, "Entidade não encontrada: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(errorMessage));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ResponseGlobalModel> handleIOException(IOException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put(LOG_KEY_EXCEPTION, "IOException");
		logParams.put(LOG_KEY_MESSAGE, ex.getMessage());
		logParams.put(LOG_KEY_ERROR_TYPE, "IO");

		LogManager.error(log, LogCategory.SYSTEM, "Erro de I/O: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}
	
	/**
	 * Extrai o tipo de entidade da mensagem de erro
	 */
	private String extractEntityTypeFromMessage(String message) {
		if (message == null) {
			return MessageTemplateKeys.ENTITY_GENERIC;
		}
		
		if (message.toLowerCase().contains("planta")) {
			return MessageTemplateKeys.PLANT_NOT_FOUND_ID;
		} else if (message.toLowerCase().contains("usuário") || message.toLowerCase().contains("usuario")) {
			return MessageTemplateKeys.USER_NOT_FOUND_ID;
		} else if (message.toLowerCase().contains("empresa")) {
			return MessageTemplateKeys.ENTERPRISE_NOT_FOUND_ID;
		}
		
		return MessageTemplateKeys.ENTITY_GENERIC;
	}
	
	/**
	 * Extrai o ID da mensagem de erro
	 */
	private String extractIdFromMessage(String message) {
		if (message == null) {
			return "0";
		}
		
		// Tenta encontrar um padrão comum de ID em mensagens de erro
		// Exemplo: "Planta com ID 123 não encontrada"
		String[] parts = message.split("\\s+");
		for (int i = 0; i < parts.length; i++) {
			if (i > 0 && parts[i-1].equalsIgnoreCase("ID")) {
				try {
					return parts[i];
				} catch (NumberFormatException e) {
					// Ignora se não for um número
				}
			}
		}
		
		return "0";
	}

	@ExceptionHandler(NoSuchAlgorithmException.class)
	public ResponseEntity<ResponseGlobalModel> handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "NoSuchAlgorithmException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "SECURITY");
		
		LogManager.error(log, LogCategory.SECURITY, "Algoritmo não encontrado: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ResponseGlobalModel> handleNullPointerException(NullPointerException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "NullPointerException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "CODE");
		
		LogManager.error(log, LogCategory.SYSTEM, "Referência nula: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ResponseGlobalModel> handleTokenExpiredException(TokenExpiredException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "TokenExpiredException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "TOKEN_EXPIRED");
		logParams.put("expiry", ex.getExpiredOn());
		
		LogManager.warn(log, LogCategory.SECURITY, "Token expirado: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(
				MessageConstraints.AUTH_TOKEN_EXPIRED));
	}

	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<ResponseGlobalModel> handleJWTVerificationException(JWTVerificationException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "JWTVerificationException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "TOKEN_INVALID");
		
		LogManager.error(log, LogCategory.SECURITY, "Falha na verificação do token: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(
				MessageConstraints.AUTH_INVALID_TOKEN));
	}

	@ExceptionHandler(JWTCreationException.class)
	public ResponseEntity<ResponseGlobalModel> handleJWTCreationException(JWTCreationException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "JWTCreationException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "TOKEN_CREATION");
		
		LogManager.error(log, LogCategory.SECURITY, "Falha na criação do token: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseGlobalModel> handleRuntimeException(RuntimeException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "RuntimeException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "RUNTIME");
		
		LogManager.error(log, LogCategory.SYSTEM, "Exceção em tempo de execução: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}

	@ExceptionHandler(ParseException.class)
	public ResponseEntity<ResponseGlobalModel> handleParseException(ParseException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "ParseException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "DATA_FORMAT");
		logParams.put("errorPosition", ex.getErrorOffset());
		
		LogManager.error(log, LogCategory.BUSINESS, "Erro de parse: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.VALIDATION_INVALID_FORMAT));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseGlobalModel> handleIllegalArgumentException(IllegalArgumentException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "IllegalArgumentException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "INVALID_INPUT");
		
		LogManager.error(log, LogCategory.BUSINESS, "Argumento inválido: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.VALIDATION_INVALID_FORMAT));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseGlobalModel> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "UsernameNotFoundException");
		logParams.put("message", ex.getMessage());
		logParams.put("username", extractUsernameFromMessage(ex.getMessage()));
		logParams.put("errorType", "AUTHENTICATION");
		
		LogManager.error(log, LogCategory.SECURITY, "Usuário não encontrado: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notFound(MessageResponse.responseGlobalModelError(
				MessageFormatterUtil.format(MessageConstraints.AUTH_USER_NOT_FOUND, ex.getMessage())));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseGlobalModel> handleException(Exception ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", ex.getClass().getSimpleName());
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "UNCATEGORIZED");
		
		LogManager.error(log, LogCategory.SYSTEM, 
		    MessageFormatterUtil.format(MessageConstraints.LOG_OPERATION_ERROR, "processamento de requisição", ex.getMessage()),
		    logParams, ex);
		
		return MessageResponse.internalError(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ResponseGlobalModel> handleSignatureException(SignatureException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "SignatureException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "TOKEN_SIGNATURE");
		
		LogManager.error(log, LogCategory.SECURITY, "Erro de assinatura: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.notAuthorize(MessageResponse.responseGlobalModelError(
				MessageConstraints.AUTH_INVALID_TOKEN));
	}

	@ExceptionHandler(AssertionError.class)
	public ResponseEntity<ResponseGlobalModel> handleAssertionError(AssertionError ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "AssertionError");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "ASSERTION");
		
		LogManager.error(log, LogCategory.SYSTEM, "Erro de asserção: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.internalError(MessageResponse.responseGlobalModelError(
				MessageConstraints.ERROR_GENERIC));
	}
	
	/**
	 * Extrai o nome de usuário da mensagem de erro
	 */
	private String extractUsernameFromMessage(String message) {
	    if (message == null) {
	        return "";
	    }
	    
	    // Tenta extrair o nome de usuário de mensagens como "Usuário 'username' não encontrado"
	    // Ou "User 'username' not found"
	    int startQuote = message.indexOf('\'');
	    int endQuote = message.lastIndexOf('\'');
	    
	    if (startQuote >= 0 && endQuote > startQuote) {
	        return message.substring(startQuote + 1, endQuote);
	    }
	    
	    return "";
	}

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ResponseGlobalModel> handleJsonMappingException(JsonMappingException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "JsonMappingException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "JSON_MAPPING");
		logParams.put("path", ex.getPathReference());
		
		LogManager.error(log, LogCategory.BUSINESS, "Erro de mapeamento JSON: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.VALIDATION_INVALID_FORMAT));
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<ResponseGlobalModel> handleJsonProcessingException(JsonProcessingException ex) {
		Map<String, Object> logParams = new HashMap<>();
		logParams.put("exception", "JsonProcessingException");
		logParams.put("message", ex.getMessage());
		logParams.put("errorType", "JSON_PROCESSING");
		logParams.put("location", ex.getLocation() != null ? 
		    String.format("linha %d, coluna %d", ex.getLocation().getLineNr(), ex.getLocation().getColumnNr()) : "N/A");
		
		LogManager.error(log, LogCategory.BUSINESS, "Erro de processamento JSON: " + ex.getMessage(), logParams, ex);
		
		return MessageResponse.badRequest(MessageResponse.responseGlobalModelError(
				MessageConstraints.VALIDATION_INVALID_FORMAT));
	}
}
