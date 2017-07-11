package ces.sdk.system.common;

import java.sql.Connection;
import java.sql.SQLException;

import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.system.factory.SystemFacadeFactory;

/**
 * 连接管理类, 用于获取sdk的Connection
 * 
 * <p>描述:</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月25日 下午11:31:23
 * @version 1.0.2015.0601
 */
public class ConnectionManager {

	private static SdkDataSource sdkDataSource;

	/**
	 * 初始化sdkDataSource
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月28日 下午2:27:42
	 */
	public static void init(){
		sdkDataSource = SystemFacadeFactory.newInstance().createSdkDataSource();
	}
	
	/**
	 * 获取sdk的Connection
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月25日 下午11:31:44
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		if(sdkDataSource == null){
			init();
		}
		return sdkDataSource.getConnection();
	}

}
