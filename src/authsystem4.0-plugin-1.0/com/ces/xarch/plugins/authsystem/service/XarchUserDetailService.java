/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.service</p>
 * <p>文件名:XarchUserDetailService.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-23 10:06:14
 */
package com.ces.xarch.plugins.authsystem.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.xarch.core.security.entity.SysUser;

/**
 * Spring Security3用户获取服务类.
 * <p>描述:实现根据登录名构造登录用户信息</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-07-23  10:06:14
 * @version 1.0.2013.0723
 */
@Service("XarchUserService")
public class XarchUserDetailService implements UserDetailsService {
	/** USER_ROLE(String):登录后用户具有的默认权限. */
	public static final String USER_ROLE = "USER_ROLE";
	
	/** 是否使用验证码（默认不使用）. */
	private boolean useValidateCode = false;
	/** Session中保存验证码的主键名称. */
	public static String sessionvalidateCodeKey = "_Xarch_validate_code_";
	/** 表单中存放用户输入验证码的参数名称. */
	private String validateCodeParameter = "validateCode";
	/** 登录名参数名称. */
	private String usernameParameter = "username";
	
	private UserInfoFacade facade = null;
	private RoleInfoFacade roleFacade = null;
	
	/** 系统编号. */
	private String appKey = "";
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 10:08:24
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (useValidateCode) {
			checkValidateCode();
		}
		
		//TODO 获取用户
		SysUser user = null;
		
		try {
			user = converter(getFacade().findByLoginName(username),null);
			
			if (user != null) {
				//TODO 构造权限
				//TODO 将用户ID加入权限列表，因为会存在用户ID与角色ID相同的情况，取消用户ID授权
//				List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList(USER_ROLE+","+user.getId());
				List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList(USER_ROLE);
				
				//TODO 将角色加入权限列表
				List<RoleInfo> roleList = null;
				
				if (StringUtils.isEmpty(appKey)) {
					roleList = getRoleFacade().findRoleInfosByUserId(user.getId());
				} else {
					roleList = getRoleFacade().findRoleInfosByUserId(user.getId(), appKey);
				}
				
				if (roleList != null) {
					StringBuilder sb = new StringBuilder();
					for (RoleInfo role : roleList) {
						authority.add(new SimpleGrantedAuthority(role.getRoleKey()));
						sb.append(",").append(role.getId());
						//SdkDaoFactory.createResourceInfoDao().getResInfosByRoleId(role.getId());
					}
					if (sb.length() > 0) sb.deleteCharAt(0);
					user.setRoleIds(sb.toString());
				}
				roleList = null;
				
				//TODO 将组织加入权限列表，因为会存在组织ID与角色ID相同的情况，取消组织ID授权
//				List<OrgInfo> orgList = getFacade().getOrgInfosByUserId(Long.valueOf(user.getId()));
//				
//				if (orgList != null) {
//					for (OrgInfo org : orgList) {
//						authority.add(new SimpleGrantedAuthority(String.valueOf(org.getOrganizeID())));
//					}
//				}
//				
//				orgList = null;
				user.setAuthorities(authority);
			}
		} catch (SystemFacadeException e) {
			throw new UsernameNotFoundException("从系统管理平台中获取用户【"+username+"】出现异常",e);
		}
		
		return user;
	}
	
	/**
	 * 转换用户实体.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-24  15:21:51
	 */
	private SysUser converter(UserInfo user, SysUser sysUser) {
		if (user != null) {
			if (sysUser == null) {
				sysUser = new SysUser();
			}
			
			sysUser.setId(user.getId());
			sysUser.setLoginName(user.getLoginName());
			sysUser.setName(user.getName());
			sysUser.setPassword(user.getPassword());
			sysUser.setShowOrder(user.getShowOrder());
			sysUser.setBelongOrgId(user.getBelongOrgId());
			sysUser.setAdminOrgId(user.getAdminOrgId());
			sysUser.setEnabled(true);
		}
		
		return sysUser;
	}
	
	/**
	 * 比较session中的验证码和用户输入的验证码是否相等.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:26:53
	 */
	protected void checkValidateCode() {
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		//@TODO 验证是否是通过Cookie登录，如果是通过Cookie登录则不验证验证码
		if (request.getParameter(usernameParameter) == null) {
			return;
		}
		
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException("验证码错误！");
		}
	}
	
	/**
	 * 获取用户输入的验证码.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:30:48
	 */
	protected String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(validateCodeParameter);
	}

	/**
	 * 获取Session中的验证码.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:30:50
	 */
	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(sessionvalidateCodeKey);
		return null == obj ? "" : obj.toString();
	}

	private UserInfoFacade getFacade() {
		if (facade == null) {
			facade = SystemFacadeFactory.newInstance().createUserInfoFacade();
		}
		
		return facade;
	}
	
	private RoleInfoFacade getRoleFacade() {
		if (roleFacade == null) {
			roleFacade = SystemFacadeFactory.newInstance().createRoleInfoFacade();
		}
		
		return roleFacade;
	}

	/**
	 * 设置是否使用验证码.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:10:45
	 */
	@Autowired(required=false)
	@Qualifier("useValidateCode")
	public void setUseValidateCode(Boolean useValidateCode) {
		this.useValidateCode = useValidateCode;
	}

	/**
	 * 设置表单中存放用户输入验证码的参数名称.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:15:56
	 */
	@Autowired(required=false)
	@Qualifier("validateCodeParameter")
	public void setValidateCodeParameter(String validateCodeParameter) {
		this.validateCodeParameter = validateCodeParameter;
	}

	/**
	 * 设置Session中保存验证码的主键名称.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-10  15:16:52
	 */
	@Autowired(required=false)
	@Qualifier("sessionvalidateCodeKey")
	public void setSessionvalidateCodeKey(String sessionvalidateCodeKey) {
		XarchUserDetailService.sessionvalidateCodeKey = sessionvalidateCodeKey;
	}

	/**
	 * 设置登录名参数名称.
	 * @author <a href="mailto:yangmujiang@sohu.com">Reamy(杨木江)</a>
	 * @date 2014-06-12  13:35:33
	 */
	@Autowired(required=false)
	@Qualifier("usernameParameter")
	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}
	
	/**
	 * 设置系统编号.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-13  12:32:49
	 */
	@Autowired(required=false)
	@Qualifier("authSystem_appKey")
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
