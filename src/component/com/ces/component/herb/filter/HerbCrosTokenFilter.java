package com.ces.component.herb.filter;

import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.farm.webservice.service.FarmResponseUtil;
import com.ces.utils.TokenUtils;
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
public class HerbCrosTokenFilter extends GenericFilterBean {

	private final static String AQUATIC_TOKEN_PARAM = "Herb-Token";



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
			httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, Xarch-Token, Connection");
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
        //从请求头中获取token
        String authToken = httpRequest.getHeader(AQUATIC_TOKEN_PARAM);
        if (authToken == null) {
            authToken = httpRequest.getParameter(AQUATIC_TOKEN_PARAM);
        }
        if (authToken == null) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, FarmResponseUtil.TOKEN_NOT_FOUND);
            return false;
        }

//		String[] tokens = TokenUtils.decodeToken(authToken);
//		if (tokens.length != 3) {
//			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, FarmResponseUtil.TOKEN_ERROR);
//			return false;
//		}
        String[] tokens = TokenUtils.decodeToken(authToken);
//        String expiryTime = FarmCommonUtil.tokenMap.get(authToken);
//        if(expiryTime == null){
//            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, FarmResponseUtil.TOKEN_NOT_FOUND);
//            return false;
//        }

        long tokenExpiryTime;

        try {
            tokenExpiryTime = new Long(tokens[1]).longValue();
        } catch (NumberFormatException nfe) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, com.ces.component.aquatic.webservice.service.AquaticResponseUtil.TOKEN_ERROR);
            return false;
        }

        if (isTokenExpired(tokenExpiryTime)) {
            httpResponse.getWriter().print(FarmResponseUtil.TOKEN_OUT_DATE);
            return false;
        }
        //更新token有效时间
        FarmCommonUtil.tokenMap.put(authToken, String.valueOf(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
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