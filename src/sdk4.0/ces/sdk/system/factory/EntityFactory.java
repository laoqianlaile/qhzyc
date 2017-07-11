package ces.sdk.system.factory;

import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.conf.SystemConf;
import ces.sdk.system.facade.SystemFacadeContext;

public abstract class EntityFactory {
	public SystemFacadeContext systemFacadeContext;

	private static EntityFactory instance;

//	public abstract OrgInfo createOrgInfo();
//
//	public abstract UserInfo createUserInfo();
//	
//	public abstract ResourceInfo createResource();
//
//	public abstract RoleInfo createRoleInfo();
//	
//	public abstract OpLogInfo createOpLogInfo();
//
//	public abstract SystemInfo createSystem();
	
	@SuppressWarnings("unchecked")
	public static EntityFactory newInstance() {
		if (instance == null) {
			try {
				Class entityFacadeFactory = Class.forName(SystemConf
						.getInstance().getEntityFactoryClassName());
				instance = (EntityFactory) entityFacadeFactory.newInstance();
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
}
