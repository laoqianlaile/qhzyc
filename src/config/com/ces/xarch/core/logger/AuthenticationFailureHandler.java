/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.core.utils</p>
 * <p>文件名:AuthenticationFailureHandler.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-06-09 10:24:55
 */
package com.ces.xarch.core.logger;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.ces.utils.WebUtils;
import com.ces.xarch.core.entity.BusinessLogEntity;

/**
 * 登录失败处理器.
 * <p>描述:负责记录用户访问日志</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-06-09  10:24:55
 * @version 1.0.2013.0609
 * 
 * 复写  xarch-1.1.0.jar 中AuthenticationFailureHandler.class
 */
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Resource(name="businessLoggerService")
	private LoggerService logger;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-09 10:25:10
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		BusinessLogEntity log = new BusinessLogEntity();
		log.setModel("登录系统");
		log.setAction("登录");
		log.setIp(WebUtils.getIpAddr(request));
		log.setOpTarget("登录名："+exception.getAuthentication().getName()+" 密码："+request.getParameter("password"));
		log.setResult("失败");
		log.setTime(new Date());
		logger.save(log);
		// 登录提示信息
		if (null != exception && null != exception.getCause() 
		        && exception.getCause() instanceof LockedException) {
		    request.getSession().setAttribute("message_login", exception.getMessage());
		} else {
		    request.getSession().setAttribute("message_login", "");
		}
		
		super.onAuthenticationFailure(request, response, exception);
	}
}
