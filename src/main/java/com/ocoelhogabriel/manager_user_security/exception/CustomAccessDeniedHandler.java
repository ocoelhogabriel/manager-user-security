package com.ocoelhogabriel.manager_user_security.exception;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	
	public CustomAccessDeniedHandler() {
		// Construtor padrão sem dependências
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(MessageConstraints.SECURITY_ACCESS_DENIED + 
				" Path: " + request.getRequestURI());
		}
		
		ResponseGlobalModel globalResponse = new ResponseGlobalModel(
			true, 
			MessageConstraints.SECURITY_ACCESS_DENIED,
			Utils.convertDateToString(new Date())
		);
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(globalResponse));
	}

}
