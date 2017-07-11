package ces.sdk.system.dbfacade;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.system.factory.SystemFacadeFactory;
import ces.sdk.util.PropertyUtil;

public class BaseFacade {
	static Log log = LogFactory.getLog(BaseFacade.class);
	protected static Properties sql_Properties = PropertyUtil.getInstance().getSql_Properties();
	protected static Properties commonSql_Properties = PropertyUtil.getInstance().getCommonSql_Properties();
	
	/** 新增状态 */
	protected final static boolean ADD_STATUS = true;
	/** 修改状态 */
	protected final static boolean UPDATE_STATUS = false;
	/** 查询总数 */
	protected static String COUNT = null;
	/** 根据showOrder升序排序 */
	protected static String ORDER_BY_SHOWORDER_ASC = null;
	/** 根据showOrder降序排序 */
	protected static String ORDER_BY_SHOWORDER_DESC = null;
	/** 排序 */
	protected static String ORDER_BY_SHOWORDER = null;
	/** 查询最大排序值 */
	protected static String MAX_ORDER = null;
	/** 组织表名称 */
	protected static String ORG_TABLE_NAME = null;
	/** 查询表名称 */
	protected static String ORG_TYPE_TABLE_NAME = null;

	static{
		ORDER_BY_SHOWORDER = commonSql_Properties.getProperty("ORDER_BY_SHOWORDER");
		COUNT = commonSql_Properties.getProperty("COUNT");
		ORDER_BY_SHOWORDER_ASC = commonSql_Properties.getProperty("ORDER_BY_SHOWORDER_ASC");
		ORDER_BY_SHOWORDER_DESC = commonSql_Properties.getProperty("ORDER_BY_SHOWORDER_DESC");
		MAX_ORDER = commonSql_Properties.getProperty("MAX_ORDER");
		ORG_TABLE_NAME = commonSql_Properties.getProperty("ORG_TABLE_NAME");
		ORG_TYPE_TABLE_NAME = commonSql_Properties.getProperty("ORG_TYPE_TABLE_NAME");
	}
	
	public Connection getConnection() throws SQLException {
		SdkDataSource sdkDataSource = SystemFacadeFactory.newInstance()
				.createSdkDataSource();
		Connection conn = sdkDataSource.getConnection();
		return conn;
	}
	
	/**
	 * 生成UUID
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 生成UUID</p>
	 * @date 2015年6月2日 下午12:57:07
	 * @return UUID
	 */
	protected String generateUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}

}