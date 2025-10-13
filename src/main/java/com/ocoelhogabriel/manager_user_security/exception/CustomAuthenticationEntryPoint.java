package com.ocoelhogabriel.manager_user_security.exception;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageConstraints;
import com.ocoelhogabriel.manager_user_security.domain.constraints.MessageFormatterUtil;
import com.ocoelhogabriel.manager_user_security.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
	
	private CustomAuthenticationEntryPoint() {
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(MessageFormatterUtil.format(MessageConstraints.SECURITY_UNAUTHORIZED) + 
				" Path: " + request.getRequestURI());
		}
		
		ResponseGlobalModel globalResponse = new ResponseGlobalModel(
			true,
			MessageFormatterUtil.format(MessageConstraints.SECURITY_UNAUTHORIZED),
			Utils.convertDateToString(new Date())
		);
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(globalResponse));
	}

}
