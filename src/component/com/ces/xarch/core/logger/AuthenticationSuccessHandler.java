//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ces.xarch.core.logger;

import com.ces.utils.WebUtils;
import com.ces.xarch.core.entity.BusinessLogEntity;
import com.ces.xarch.core.security.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.ELRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Resource(
			name = "businessLoggerService"
	)
	private LoggerService logger;
	public static final String LOGINUSERNAME_COOKIEKEY = "_xarch_loginuser_name_";

	public AuthenticationSuccessHandler() {
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		BusinessLogEntity log = new BusinessLogEntity();
		log.setModel("登录系统");
		log.setAction("登录");
		log.setIp(WebUtils.getIpAddr(request));
		log.setOpTarget("用户名：" + ((SysUser) authentication.getPrincipal()).getName());
		log.setResult("成功");
		log.setUserId(((SysUser) authentication.getPrincipal()).getId());
		log.setTime(new Date());
		log.setUrl(request.getRequestURI());
		this.logger.save(log);
		Cookie cookie = new Cookie("_xarch_loginuser_name_", URLEncoder.encode(((SysUser) authentication.getPrincipal()).getName(), "UTF-8"));
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
		if (isAjaxRequest(request)) {
			sendJsonResponse(response, "success", "true");
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	private static final RequestMatcher REQUEST_MATCHER = new ELRequestMatcher("hasHeader('X-Requested-With','XMLHttpRequest')");

	public static final String JSON_VALUE = "{\"%s\": %s}";


	public Boolean isAjaxRequest(HttpServletRequest request) {
		return REQUEST_MATCHER.matches(request);
	}

	public void sendJsonResponse(HttpServletResponse response, String key, String message) {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			response.getWriter().write(String.format(JSON_VALUE, key, message));
		} catch (IOException e) {
//            logger.error("error writing json to response", e);
		}
	}
}
