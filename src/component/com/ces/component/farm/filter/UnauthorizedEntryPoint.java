package com.ces.component.farm.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.sendError(
				HttpServletResponse.SC_FORBIDDEN,
				"Unauthorized: Authentication token was either missing or invalid.");
	}
}
