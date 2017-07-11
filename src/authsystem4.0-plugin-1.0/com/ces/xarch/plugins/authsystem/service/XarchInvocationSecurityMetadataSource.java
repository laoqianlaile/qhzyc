/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.service</p>
 * <p>文件名:XarchInvocationSecurityMetadataSource.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-23 10:10:21
 */
package com.ces.xarch.plugins.authsystem.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.utils.StringUtils;
import com.ces.xarch.core.utils.AntPathRequestMatcher;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * Spring Security3 资源权限管理类.
 * <p>描述:负责根据访问资源构造其权限</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-07-23  10:10:21
 * @version 1.0.2013.0723
 */
@Service("XarchInvocationSecurityMetadataSource")
public class XarchInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	/** SAME_LEVEL_TYPE(String): 忽略参数. */
	private static final String SAME_LEVEL_TYPE = "02";
	/** ALL_TYPE(String):忽略子目录. */
	private static final String ALL_TYPE = "03";
	/** USER_TYPE(String):登录后可访问. */
	private static final String USER_TYPE = "04";
	
	/** resourceMap(Map<String,Collection<ConfigAttribute>>):资源权限缓存集合. */
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	/** denyAll(boolean):默认拒绝所有访问. */
	private boolean denyAll = true;
	/** 每次都从数据库获取资源权限. */
	private boolean awaysLoadFromDB = false;
	/** 进行一次刷新. */
	private static boolean doFlush = false;
	/** denyAttr(Collection<ConfigAttribute>):拒绝访问时返回的权限. */
	private static Collection<ConfigAttribute> denyAttr = null;
	
	/** 系统编号. */
	private String appKey = "";
	
	/**
	 * 清除缓存.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-04-09  14:34:12
	 */
	private Map<String, Collection<ConfigAttribute>> clearCache() {
		try {
			return loadResourceDefine();
		} catch (SystemFacadeException e) {
			throw new RuntimeException("从系统管理平台获取角色及资源信息出错", e);
		}
	}
	
	/**
	 * 刷新缓存.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-02  16:59:50
	 */
	public static synchronized void reFlush() {
		doFlush = true;
	}
	
	/**
	 * 检查是否要刷新缓存.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-02  17:02:01
	 */
	private synchronized Map<String, Collection<ConfigAttribute>> checkIfNeedDoFlush() {
		if (doFlush) {
			doFlush = false;
			resourceMap = clearCache();
		}
		
		if (resourceMap != null && resourceMap.size() > 0) {
			Map<String, Collection<ConfigAttribute>> resourceMapClone = new HashMap<String, Collection<ConfigAttribute>>(resourceMap.size());
			resourceMapClone.putAll(resourceMap);
			return resourceMapClone;
		}
		
		return null;
	}
	
	@Autowired(required=false)
	@Qualifier("securityDenyAll")
	public void setDenyAll(Boolean denyAll) {
		this.denyAll = denyAll;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.access.SecurityMetadataSource#getAttributes(java.lang.Object)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 10:26:13
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		Map<String, Collection<ConfigAttribute>> resourceMap = null;
		
		if (awaysLoadFromDB) {
			resourceMap = clearCache();
		} else {
			resourceMap = checkIfNeedDoFlush();
		}
		
        if (resourceMap != null) {
        	Iterator<String> ite = resourceMap.keySet().iterator();
        	Collection<ConfigAttribute> atts = null;
        	
        	while (ite.hasNext()) {
                String resURL = ite.next();
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(resURL);
                
                if (matcher.matches(object)) {
                	if (atts == null) {
                		atts = new ArrayList<ConfigAttribute>();
                	}
                	
                	atts.addAll(resourceMap.get(resURL));
                }
            }
        	
        	if (atts != null) {
        		return atts;
        	}
        }
        
        if (denyAll) {
        	return denyAttr;
        }
        
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.SecurityMetadataSource#getAllConfigAttributes()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 10:26:13
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		if (resourceMap == null) {
			try {
				resourceMap = loadResourceDefine();
			} catch (SystemFacadeException e) {
				throw new RuntimeException("从系统管理平台获取角色及资源信息出错", e);
			}
		}
		if (denyAttr == null) {
			denyAttr = new ArrayList<ConfigAttribute>(1);
			denyAttr.add(new SecurityConfig("--DENYALL--"));
		}
		
		return null;
	}
	
	/**
	 * 加载系统资源权限.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @throws SystemFacadeException 
	 * @throws SystemFacadeException 当从系统管理平台获取角色及资源信息出错时抛出
	 * @date 2013-04-09  14:50:04
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Collection<ConfigAttribute>> loadResourceDefine() throws SystemFacadeException {
		if (!XarchListener.isPluginInit("authsystemPluginFit")) {
			reFlush();
			return null;
		}
		
		RoleInfoFacade roleInfoFacade = SystemFacadeFactory.newInstance().createRoleInfoFacade();
		ResourceInfoFacade resourceInfoFacade = SystemFacadeFactory.newInstance().createResourceInfoFacade();
		//TODO 获取所有可用角色及其拥有的资源
		List<RoleInfo> roleList = null;
		
		if (StringUtils.isEmpty(appKey)) {
			roleList = roleInfoFacade.findAllRoleInfos();
		} else {
			roleList = roleInfoFacade.findRolesBySysCode(appKey);
		}
		
		Map<String, List<ResourceInfo>> resMap = new HashMap<String, List<ResourceInfo>>();
		
		if (roleList != null && !roleList.isEmpty()) {
			for (RoleInfo role : roleList) {
				List<ResourceInfo> resList = resourceInfoFacade.findResInfosByRoleId(role.getId());
				
				if (resList != null && !resList.isEmpty()) {
					resMap.put(role.getRoleKey(), resList);
				}
				
				resList = null;
			}
		}
		
		roleList = null;
		Map<String, Collection<ConfigAttribute>> resourceMap = null;
		
		if (!resMap.isEmpty()) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			Collection<ConfigAttribute> atts = null;
			String resUrl = null;
			
			for (String role : resMap.keySet()) {
				List<ResourceInfo> resList = resMap.get(role);
				
				for (ResourceInfo res : resList) {
					resUrl = res.getUrl();
					String suf = "";
					
					if (SAME_LEVEL_TYPE.equals(res.getComments())) {
						suf = "*";
					}
					if (ALL_TYPE.equals(res.getComments())) {
						suf = "**/**";
					}
					
					if (resourceMap.containsKey(resUrl+suf)) {
						atts = resourceMap.get(resUrl+suf);
					} else {
						atts = new ArrayList<ConfigAttribute>();
						resourceMap.put(resUrl+suf, atts);
					}
					
			        atts.add(new SecurityConfig(role));
			        
			        if (USER_TYPE.equals(res.getResourceTypeName()) && !XarchUserDetailService.USER_ROLE.equals(role)) {
			        	atts.add(new SecurityConfig(XarchUserDetailService.USER_ROLE));
			        }
				}
			}
			
			atts = null;
		}
		
		resMap = null;
		return resourceMap;
	}

	/**
	 * 设置是否每次均从数据库动态获取权限，默认为false.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-02  16:54:45
	 */
	@Autowired(required=false)
	@Qualifier("securityAwaysLoadFromDB")
	public void setAwaysLoadFromDB(Boolean awaysLoadFromDB) {
		this.awaysLoadFromDB = awaysLoadFromDB;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.SecurityMetadataSource#supports(java.lang.Class)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 10:26:13
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
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
