package com.ces.component.aquatic.filter;

import com.ces.component.aquatic.dto.JyshDto;
import com.ces.component.aquatic.utils.AquaticUserUtil;
import com.ces.component.aquatic.webservice.service.AquaticResponseUtil;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.utils.TokenUtils;
import com.ces.xarch.core.security.entity.SysUser;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 黄翔宇 on 15/6/4.
 */
public class AquaticCrosTokenFilter extends GenericFilterBean {

	private final static String AQUATIC_TOKEN_PARAM = "Aquatic-Token";

	private final static String AQUATIC_USER_TYPE = "User-Type";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (!"OPTIONS".equals(httpRequest.getMethod())) {
			if (!checkAuth(httpRequest, httpResponse)) {
				return;
			}
		} else {
			httpResponse.addHeader("Access-Control-Allow-Origin", "*");
			httpResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
			httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, Xarch-Token");
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 检查token信息
	 *
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 */
	private boolean checkAuth(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

		String userType = httpRequest.getHeader(AQUATIC_USER_TYPE);
		if (userType == null) {
			userType = httpRequest.getParameter(AQUATIC_USER_TYPE);
		}
		if (userType == null || !"1,2".contains(userType)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.QUERY_TYPE_ERROR);
			return false;
		}

		//从请求头中获取token
		String authToken = httpRequest.getHeader(AQUATIC_TOKEN_PARAM);
		if (authToken == null) {
			authToken = httpRequest.getParameter(AQUATIC_TOKEN_PARAM);
		}
		if (authToken == null) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.TOKEN_NOT_FOUND);
			return false;
		}
		String[] tokens = TokenUtils.decodeToken(authToken);
		if (tokens.length != 3) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.TOKEN_ERROR);
			return false;
		}

		long tokenExpiryTime;

		try {
			tokenExpiryTime = new Long(tokens[1]).longValue();
		} catch (NumberFormatException nfe) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.TOKEN_ERROR);
			return false;
		}

		if (isTokenExpired(tokenExpiryTime)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.TOKEN_ERROR);
			return false;
		}

		JyshDto jyshDto = AquaticUserUtil.loadUser(tokens[0], userType);

		if (jyshDto == null) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, AquaticResponseUtil.TOKEN_ERROR);
			return false;
		}

		return true;
	}

	protected boolean isTokenExpired(long tokenExpiryTime) {
		return tokenExpiryTime < System.currentTimeMillis();
	}

	private static boolean equals(String expected, String actual) {
		byte[] expectedBytes = bytesUtf8(expected);
		byte[] actualBytes = bytesUtf8(actual);
		if (expectedBytes.length != actualBytes.length) {
			return false;
		}

		int result = 0;
		for (int i = 0; i < expectedBytes.length; i++) {
			result |= expectedBytes[i] ^ actualBytes[i];
		}
		return result == 0;
	}

	private static byte[] bytesUtf8(String s) {
		if (s == null) {
			return null;
		}
		return Utf8.encode(s);
	}

}