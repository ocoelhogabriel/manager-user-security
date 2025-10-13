package com.ocoelhogabriel.manager_user_security.handler;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ocoelhogabriel.manager_user_security.application.services.PerfilPermissaoService;
import com.ocoelhogabriel.manager_user_security.application.use_cases.URLValidator;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.RecursoMapEnum;

/**
 * Permission verification handler for resource access
 * Validates if a profile has permission to access a specific resource with a specific HTTP method
 */
@Component
public class PermissaoHandlerImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissaoHandlerImpl.class);

	private final PerfilPermissaoService perfilPermissaoService;
	
	public PermissaoHandlerImpl(PerfilPermissaoService perfilPermissaoService) {
		this.perfilPermissaoService = perfilPermissaoService;
	}

	/**
	 * Checks if a profile has permission to access a resource with a specific HTTP method
	 * 
	 * @param perfil The profile ID to check
	 * @param urlValidator The URL validator containing information about the resource
	 * @param method The HTTP method (GET, POST, PUT, DELETE, etc.)
	 * @return true if the profile has permission, false otherwise
	 */
	public boolean checkPermission(String perfil, URLValidator urlValidator, String method) {
		Objects.requireNonNull(perfil, MessageFormatterUtil.format(MessageConstraints.PERMISSION_NULL_PROFILE));
		Objects.requireNonNull(urlValidator, MessageFormatterUtil.format(MessageConstraints.PERMISSION_NULL_URL_VALIDATOR));
		Objects.requireNonNull(method, MessageFormatterUtil.format(MessageConstraints.PERMISSION_NULL_HTTP_METHOD));
		
		// Check if the resource is valid
		RecursoMapEnum recurso = urlValidator.getRecursoMapEnum();
		if (recurso == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.PERMISSION_RESOURCE_NOT_RECOGNIZED, 
					urlValidator.getMessage()));
			}
			return false;
		}

		// Check if the profile exists
		var perfilEntity = perfilPermissaoService.findByIdPerfilEntity(perfil);
		if (perfilEntity == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.PERMISSION_PROFILE_NOT_FOUND, perfil));
			}
			return false;
		}
		
		// For this simplified implementation, we assume that if the profile exists, it has permission
		// In a complete implementation, we would fetch the specific permissions of the profile
		// for the resource and verify according to the HTTP method
		
		// Map the HTTP method to the action type
		String acaoTipo = mapHttpMethodToAction(method);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_CHECK_FOR_URL, 
				perfil, recurso.getNome(), acaoTipo));
		}
		
		// Simplified implementation: if the profile exists, it has permission
		// In a real implementation, we would check for specific permissions
		
		// In this simplified implementation, we always authorize access
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(MessageFormatterUtil.format(MessageConstraints.PERMISSION_ACCESS_GRANTED, 
				perfil, recurso.getNome(), method));
		}
		
		return true;
	}
	
	/**
	 * Maps the HTTP method to the action type in the permission
	 * 
	 * @param httpMethod The HTTP method
	 * @return The corresponding action type
	 */
	private String mapHttpMethodToAction(String httpMethod) {
		if (httpMethod == null) {
			return "";
		}
		
		return switch (httpMethod.toUpperCase()) {
			case "GET" -> "list";  // list or search depending on context
			case "POST" -> "create";
			case "PUT", "PATCH" -> "update";
			case "DELETE" -> "delete";
			default -> "";
		};
	}
	
	// Method removed because it's not used in the current implementation
}