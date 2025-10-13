package com.ocoelhogabriel.manager_user_security.application.use_cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.AcaoRecursoMapEnum;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.RecursoMapEnum;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.ServerMapEnum;
import com.ocoelhogabriel.manager_user_security.domain.value_objects.VersaoMapEnum;

/**
 * Validates API URLs and maps them to system resources
 * Implements URL validation for resource-based access control
 */
public class URLValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(URLValidator.class);

	private RecursoMapEnum recursoMapEnum;
	private String message;

	public URLValidator(RecursoMapEnum recursoMapEnum, String message) {
		this.recursoMapEnum = recursoMapEnum;
		this.message = message;
	}

	public static URLValidator validateURL(String url, String method) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Validating URL: {} [{}]", url, method);
			}
			
			// Basic URL validation
			if (url == null || url.isEmpty()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Empty or null URL");
				}
				return new URLValidator(null, MessageConstraints.SECURITY_INVALID_URL);
			}

			String[] parts = url.split("/");

			// Verificação da estrutura mínima da URL
			if (parts.length < 5) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(MessageConstraints.SECURITY_INVALID_URL, url);
				}
				return new URLValidator(null, MessageConstraints.SECURITY_INVALID_URL);
			}

			// Extract URL parts
			String serverPart = "/" + parts[1];
			String recursoPart = "/api/" + parts[3];
			String versionPart = "/" + parts[4];
			String actionPart = parts.length > 5 ? "/" + parts[5] : null;

			// Map to corresponding enums
			String server = ServerMapEnum.mapDescricaoToServer(serverPart.toUpperCase());
			String recurso = RecursoMapEnum.mapUrlToUrl(recursoPart.toUpperCase());
			RecursoMapEnum recursoEnum = RecursoMapEnum.mapUrlToRecursoMapEnum(recursoPart.toUpperCase());
			String version = VersaoMapEnum.mapDescricaoToVersao(versionPart.toUpperCase());
			String action = "";
			if (actionPart != null) {
				action = AcaoRecursoMapEnum.mapDescricaoToAction(actionPart.toUpperCase());
			}

			// Validate main URL components
			if (server == null || recurso == null || version == null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(MessageConstraints.PERMISSION_RESOURCE_NOT_RECOGNIZED,
						String.format("Server: %s, Resource: %s, Version: %s", server, recurso, version));
				}
				return new URLValidator(null, MessageConstraints.PERMISSION_RESOURCE_NOT_RECOGNIZED);
			}

			// Special handling for GETs with ID in the path
			if (method.equalsIgnoreCase("GET") && actionPart != null && action == null) {
				// Validation for when the action is an ID (search by ID)
				if (!actionPart.matches("/\\d+")) {
					String errorMessage = "Invalid URL for FIND action: missing or invalid code.";
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(errorMessage);
					}
					return new URLValidator(recursoEnum, errorMessage);
				}
				return new URLValidator(recursoEnum, "FIND");
			}

			String successMessage = "Valid URL! Method: " + method + " Server: " + server + 
			                       ", Resource: " + recurso + ", Version: " + version + ", Action: " + action;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(successMessage);
			}
			return new URLValidator(recursoEnum, successMessage);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Error validating URL: {} - {}", url, e.getMessage(), e);
			}
			return new URLValidator(null, MessageConstraints.PERMISSION_RESOURCE_NOT_RECOGNIZED);
		}
	}

	public RecursoMapEnum getRecursoMapEnum() {
		return recursoMapEnum;
	}

	public void setRecursoMapEnum(RecursoMapEnum recursoMapEnum) {
		this.recursoMapEnum = recursoMapEnum;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
