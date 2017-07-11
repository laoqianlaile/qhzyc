/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem</p>
 * <p>文件名:XarchFit.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-23 15:03:26
 */
package com.ces.xarch.plugins.authsystem.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ces.sdk.system.conf.SystemConf;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.xarch.core.plugin.XarchPluginFit;

/**
 * 系统管理平台适配管理类.
 * <p>描述:负责整合配置文件</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-07-23  15:03:26
 * @version 1.0.2013.0723
 */
@Component
public class AuthsystemPluginFit implements XarchPluginFit {
	/** 实体工厂类键值. */
	private final String ENTITY_FACTORY_CLASS = "entity-factory-class-name";
	/** 代理工厂类键值. */
	private final String FACADE_FACTORY_CLASS = "facade-factory-class-name";
	/** 权限引擎类键值. */
	private final String AUTH_ENGINE_CLASS = "auth-engine-class-name";
	
	/** 默认实体工厂类. */
	private final String DEFAULT_ENTITY_FACTORY_CLASS = "ces.sdk.system.dbfactory.DbEntityFactory";
	/** 默认代理工厂类. */
	private final String DEFAULT_FACADE_FACTORY_CLASS = "com.ces.xarch.plugins.authsystem.facade.XarchSystemFacadeFactory";
	/** 默认权限引擎类. */
	private final String DEFAULT_AUTH_ENGINE_CLASS = "ces.sdk.system.engine.NoCacheAuthEngine";
	
	/** 系统管理平台 sdk 配置信息. */
	private Map<String, Object> sdkConf;
	
	/* (non-Javadoc)
	 * @see com.ces.xarch.core.plugin.XarchPluginFit#fit()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 15:21:36
	 */
	@Override
	public void fit() {
		SystemConf conf;
		
		try {
			conf = SystemConf.getInstance();
		} catch (Exception ex) {
			// 无需抛出异常，此处故意让系统管理平台进行一次实例化，因为配置文件不存在故一定会出现异常
		}
		
		conf = SystemConf.getInstance();
		
		//初始化参数
		if (sdkConf != null && sdkConf.containsKey(ENTITY_FACTORY_CLASS)) {
			conf.setEntityFactoryClassName((String)sdkConf.get(ENTITY_FACTORY_CLASS));
		} else {
			conf.setEntityFactoryClassName(DEFAULT_ENTITY_FACTORY_CLASS);
		}
		if (sdkConf != null && sdkConf.containsKey(FACADE_FACTORY_CLASS)) {
			conf.setFacadeFactoryClassName((String)sdkConf.get(FACADE_FACTORY_CLASS));
		} else {
			conf.setFacadeFactoryClassName(DEFAULT_FACADE_FACTORY_CLASS);
		}
		if (sdkConf != null && sdkConf.containsKey(AUTH_ENGINE_CLASS)) {
			conf.setAuthEngineClassName((String)sdkConf.get(AUTH_ENGINE_CLASS));
		} else {
			conf.setAuthEngineClassName(DEFAULT_AUTH_ENGINE_CLASS);
		}
		
		//初始化实例
		SystemFacadeFactory.newInstance();
	}

	@SuppressWarnings("unchecked")
	@Autowired(required=false)
	@Qualifier("authsystem_sdk_conf")
	public void setSdkConf(Map<String, Object> sdkConf) {
		this.sdkConf = (Map<String, Object>)sdkConf.get("authsystem_sdk_conf");
	}
}
