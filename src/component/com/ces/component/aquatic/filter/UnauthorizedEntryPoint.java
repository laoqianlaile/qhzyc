package com.ces.component.aquatic.filter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 黄翔宇 on 15/6/4.
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.sendError(
				HttpServletResponse.SC_FORBIDDEN,
				"Unauthorized: Authentication token was either missing or invalid.");
	}
}
