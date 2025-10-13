package com.ocoelhogabriel.manager_user_security.utils.message;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(MessageConstraints.SECURITY_ACCESS_DENIED + " URL: {}, User: {}", 
				request.getRequestURL(), request.getRemoteUser(), e);
		}
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write("{\"error\": \"" + MessageConstraints.SECURITY_ACCESS_DENIED + "\"}");
	}
}
