/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.web.session;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;


/**
 * Filter required by concurrent session handling package.
 * <p>
 * This filter performs two functions. First, it calls
 * {@link SessionRegistry#refreshLastRequest(String)} for each request
 * so that registered sessions always have a correct "last update" date/time. Second, it retrieves a
 * {@link SessionInformation} from the <code>SessionRegistry</code>
 * for each request and checks if the session has been marked as expired.
 * If it has been marked as expired, the configured logout handlers will be called (as happens with
 * {@link org.springframework.security.web.authentication.logout.LogoutFilter}), typically to invalidate the session.
 * A redirect to the expiredURL specified will be performed, and the session invalidation will cause an
 * {@link HttpSessionDestroyedEvent} to be published via the
 * {@link HttpSessionEventPublisher} registered in <code>web.xml</code>.</p>
 *
 * @author Ben Alex
 */
public class ConcurrentSessionFilter extends GenericFilterBean {
	//~ Instance fields ================================================================================================

	private SessionRegistry sessionRegistry;
	private String expiredUrl;
	private LogoutHandler[] handlers = new LogoutHandler[]{new SecurityContextLogoutHandler()};
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	//~ Methods ========================================================================================================


	/**
	 * @deprecated Use constructor which injects the <tt>SessionRegistry</tt>.
	 */
	public ConcurrentSessionFilter() {
	}

	public ConcurrentSessionFilter(SessionRegistry sessionRegistry) {
		this(sessionRegistry, null);
	}

	public ConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
		this.sessionRegistry = sessionRegistry;
		this.expiredUrl = expiredUrl;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionRegistry, "SessionRegistry required");
		Assert.isTrue(expiredUrl == null || UrlUtils.isValidRedirectUrl(expiredUrl),
				expiredUrl + " isn't a valid redirect URL");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		if (session != null) {
			SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
			if (info != null) {
				if (info.isExpired()) {
					// Expired - abort processing
					doLogout(request, response);
					//在响应头设置session状态
					session.setMaxInactiveInterval(0);
					if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
						processAjax(request, response, false);
						System.out.println("info.isExpired is true==================================");
					} else {
						String targetUrl = determineExpiredUrl(request, info);
						if (targetUrl != null) {
							redirectStrategy.sendRedirect(request, response, targetUrl);
						} else {
							response.getWriter().print("This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).");
							response.flushBuffer();
						}
					}
					return;
				} else {
					// Non-expired - update last request date/time
					sessionRegistry.refreshLastRequest(info.getSessionId());
				}
			} else if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
				if ("TRACE".equalsIgnoreCase(request.getHeader("TRACE-SASS-AJAX-LOGIN"))) {//ajax登录特殊请求
					System.out.print("TRACE-SASS-AJAX-LOGIN");
				} else {
					// Expired - abort processing
					doLogout(request, response);
					//在响应头设置session状态
					session.setMaxInactiveInterval(0);
					processAjax(request, response, true);
					System.out.println("info is null==================================");
					return;
				}

			}
		} else if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
			// Expired - abort processing
			doLogout(request, response);
			processAjax(request, response, true);
			System.out.println("session is null==================================");
			//return;
		} else if ("/logout".equals(request.getServletPath())) {
			// 注销session失效时，转向登录页面
			redirectStrategy.sendRedirect(request, response, expiredUrl);
			return;
		}
		chain.doFilter(request, response);
	}

	private void processAjax(HttpServletRequest request, HttpServletResponse response, boolean isNullSession) throws IOException {
		//如果是ajax请求响应头会有，x-requested-with；
		String referer = request.getHeader("referer");
		if (null == referer || "".equals(referer) || referer.indexOf("/dhtmlx/") > 0/*配置平台跳转问题*/) {
			referer = request.getContextPath() + expiredUrl;
		}
		if (isNullSession) {
			request.getSession(true);
		}
		response.setHeader("location", referer);
		response.setHeader("session-status", "timeout");
		boolean isCfgSystem = (referer.indexOf("cfg.jsp") > 0);
		PrintWriter writer = null;
		if (isCfgSystem) {
			writer = response.getWriter();
			writer.print("{\"location\":\"" + referer + "\",\"session-status\":\"timeout\"}");
			writer.flush();
			writer.close();
		} else if (isNullSession) {
			writer = response.getWriter();
			writer.print("");
			writer.flush();
			writer.close();
		}
		writer = null;
	}

	protected String determineExpiredUrl(HttpServletRequest request, SessionInformation info) {
		return expiredUrl;
	}

	private void doLogout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		for (LogoutHandler handler : handlers) {
			handler.logout(request, response, auth);
		}
	}

	/**
	 * @deprecated use constructor injection instead
	 */
	@Deprecated
	public void setExpiredUrl(String expiredUrl) {
		this.expiredUrl = expiredUrl;
	}

	/**
	 * @deprecated use constructor injection instead
	 */
	@Deprecated
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setLogoutHandlers(LogoutHandler[] handlers) {
		Assert.notNull(handlers);
		this.handlers = handlers;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}
