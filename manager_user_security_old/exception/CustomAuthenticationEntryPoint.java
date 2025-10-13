package com.ocoelhogabriel.manager_user_security.exception;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.gson.Gson;

import com.ocoelhogabriel.manager_user_security.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ResponseGlobalModel globalResponse = new ResponseGlobalModel(true, authException.getMessage(), Utils.convertDateToString(new Date()));
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(globalResponse));
	}

}
