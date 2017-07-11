package com.ces.component.sensor.filter;

import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.sensor.service.SensorResponseUtil;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SensorCrosTokenFilter extends GenericFilterBean{
	
	private final static String FARM_TOKEN_PARAM = "Sensor-Token";
	
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
		//从请求头中获取token
		String authToken = httpRequest.getHeader(FARM_TOKEN_PARAM);
		if (authToken == null) {
			authToken = httpRequest.getParameter(FARM_TOKEN_PARAM);
		}
		if (authToken == null) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, SensorResponseUtil.TOKEN_NOT_FOUND);
			return false;
		}
		
//		String[] tokens = TokenUtils.decodeToken(authToken);
//		if (tokens.length != 3) {
//			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, SensorResponseUtil.TOKEN_ERROR);
//			return false;
//		}
		String expiryTime = FarmCommonUtil.tokenMap.get(authToken);
		if(expiryTime == null){
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, SensorResponseUtil.TOKEN_NOT_FOUND);
			return false;
		}

		long tokenExpiryTime;

		try {
			tokenExpiryTime = new Long(expiryTime).longValue();
		} catch (NumberFormatException nfe) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, SensorResponseUtil.TOKEN_ERROR);
			return false;
		}

		if (isTokenExpired(tokenExpiryTime)) {
			httpResponse.getWriter().print(SensorResponseUtil.TOKEN_OUT_DATE);
			return false;
		}
		//更新token有效时间
		FarmCommonUtil.tokenMap.put(authToken, String.valueOf(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		
		return true;
	}
	
	protected boolean isTokenExpired(long tokenExpiryTime) {
		return tokenExpiryTime < System.currentTimeMillis();
	}
		
}
