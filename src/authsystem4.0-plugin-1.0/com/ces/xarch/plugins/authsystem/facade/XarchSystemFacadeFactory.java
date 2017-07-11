/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.facade</p>
 * <p>文件名:XarchSystemFacadeFactory.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-23 11:43:10
 */
package com.ces.xarch.plugins.authsystem.facade;

import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.system.dbfactory.DbSystemFacadeFactory;

import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.plugins.authsystem.service.AuthsystemDataSource;

/**
 * 系统管理平台3.5代理工厂实现类.
 * <p>描述:该类获得所需操作代理类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-07-23  11:43:10
 * @version 1.0.2013.0723
 */
public class XarchSystemFacadeFactory extends DbSystemFacadeFactory {
	/* (non-Javadoc)
	 * @see ces.sdk.system.factory.SystemFacadeFactory#createSdkDataSource()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 17:19:32
	 */
	@Override
	public SdkDataSource createSdkDataSource() {
		return (SdkDataSource)XarchListener.getBean(AuthsystemDataSource.class);
	}
}
