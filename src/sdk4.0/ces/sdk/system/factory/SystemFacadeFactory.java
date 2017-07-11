package ces.sdk.system.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.system.common.ShareCache;
import ces.sdk.system.conf.SystemConf;
import ces.sdk.system.dbfactory.DbSystemFacadeFactory;
import ces.sdk.system.facade.OpLogInfoFacade;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.OrgTypeInfoFacade;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.facade.SystemFacadeContext;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.util.CesGlobals;

/**
 * 代理工厂类.<br>
 * 该类获得所需操作代理类
 * @author Administrator
 *
 */
public abstract class SystemFacadeFactory {
	protected Log log = LogFactory.getLog(SystemFacadeFactory.class);

	private static SystemFacadeFactory instance;

	/**
	 * 获得 资源操作代理对象类
	 * @return ResourceInfoFacade
	 */
	public abstract ResourceInfoFacade createResourceInfoFacade();

	/**
	 * 获得 组织操作代理对象类
	 * @return OrgInfoFacade
	 */
	public abstract OrgInfoFacade createOrgInfoFacade();

	/**
	 * 获得 用户操作代理对象类
	 * @return UserInfoFacade
	 */
	public abstract UserInfoFacade createUserInfoFacade();

	/**
	 * 获得 角色操作代理对象类
	 * @return RoleInfoFacade
	 */
	public abstract RoleInfoFacade createRoleInfoFacade();

	/**
	 * 获得 日志操作代理对象类
	 * @return OpLogInfoFacade
	 */
	public abstract OpLogInfoFacade createOpLogInfoFacade();
	
	/**
	 * 获得组织级别
	 * @return OrgTypeFacade
	 */
	public abstract OrgTypeInfoFacade createOrgTypeFacade();
	/**
	 * 获得系统
	 * @return SystemFacade
	 */
	public abstract SystemFacade createSystemFacade();
	
	/**
	 * 获得数据DATASOURCE
	 * @return SdkDataSource
	 */
	@SuppressWarnings("unchecked")
	public SdkDataSource createSdkDataSource() {
		try {
			Class sdkDataSourceClass = Class.forName(SystemConf.getInstance()
					.getSdkDataSourceClassName());
			SdkDataSource sdkDataSource = (SdkDataSource) sdkDataSourceClass
					.newInstance();
			return sdkDataSource;
		} catch (ClassNotFoundException classE) {
			log.error(classE.getMessage(), classE);
			throw new RuntimeException(classE.getMessage(), classE);
		} catch (InstantiationException instE) {
			log.error(instE.getMessage(), instE);
			throw new RuntimeException(instE.getMessage(), instE);
		} catch (IllegalAccessException accessE) {
			log.error(accessE.getMessage(), accessE);
			throw new RuntimeException(accessE.getMessage(), accessE);
		}
	}

	/**
	 * 创建系统代理上下文对象
	 * @return
	 */
	public SystemFacadeContext createSystemFacadeContext() {
		return new SystemFacadeContext();
	}

	/**
	 * 初始化 上下文
	 * @param systemFacadeContext
	 */
	public abstract void initSystemFacadeContext(
			SystemFacadeContext systemFacadeContext);

	/**
	 * 销毁 上下文
	 * @param systemFacadeContext
	 */
	public abstract void destorySystemFacadeContext(
			SystemFacadeContext systemFacadeContext);

	/**
	 * 获得 代理实例
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SystemFacadeFactory newInstance() {
		if (instance == null) {
			try {
				Class systemFacadeFactory = Class.forName(SystemConf
						.getInstance().getFacadeFactoryClassName());
				instance = (SystemFacadeFactory) systemFacadeFactory
						.newInstance();
				initSdkUniqueCode();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * 初始化sdk唯一标识符, 每个系统下的sdk都有唯一的sdk标识
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月30日 上午10:00:50
	 */
	public static void initSdkUniqueCode(){
		ShareCache.sdkUniqueCode = CesGlobals.getInstance().getSdkUniqueCode();
		ShareCache.initSdkUniqueCodeToDataBase();
	} 
	/**
	 * 获得facade实例
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SystemFacadeFactory newInstance(boolean readConfig) {
		if(readConfig){
			return newInstance();
		}
		if (instance == null) {
			instance = new DbSystemFacadeFactory();
		}
		return instance;
	}

}
