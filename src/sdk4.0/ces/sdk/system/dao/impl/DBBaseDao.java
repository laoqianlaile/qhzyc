package ces.sdk.system.dao.impl;

import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.dbfacade.BaseFacade;
import ces.sdk.util.PropertyUtil;

public class DBBaseDao {

	static Log log = LogFactory.getLog(BaseFacade.class);
	protected static Properties sql_Properties = PropertyUtil.getInstance().getSql_Properties();
	protected static Properties commonSql_Properties = PropertyUtil.getInstance().getCommonSql_Properties();
	
	/** 排序 */
	protected static String ORDER_BY_SHOWORDER = null;
	/** 查询总数 */
	protected static String COUNT = null;
	/** 新增状态 */
	protected final static boolean ADD_STATUS = true;
	/** 修改状态 */
	protected final static boolean UPDATE_STATUS = false;

	/** 组织级别字段名*/
	protected static String ORG_TYPE_COLUMNS_NAME = null;
	/** 组织字段名*/
	protected static String ORG_COLUMNS_NAME = null;
	/** 组织用户字段名 */
	protected static String ORG_USER_COLUMNS_NAME = null;
	/** 角色用户字段名 */
	protected static String ROLE_USER_COLUMNS_NAME = null;
	/** 组织角色字段名 */
	protected static String ORG_ROLE_COLUMNS_NAME = null;
	/** 系统字段名 */
	protected static String SYSTEM_COLUMNS_NAME = null;
	/** 系统资源字段名 */
	protected static String SYSTEM_RES_COLUMNS_NAME = null;
	/** 系统角色字段名 */
	protected static String SYSTEM_ROLE_COLUMNS_NAME = null;
	/** 资源字段名 */
	protected static String RESOURCE_COLUMNS_NAME = null;
	/** 角色资源字段名 */
	protected static String ROLE_RES_COLUMNS_NAME = null;
	/** 角色字段名 */
	protected static String ROLE_COLUMNS_NAME = null;

	/** 组织级别表名称 */
	protected static String ORG_TYPE_TABLE_NAME = null;
	/** 组织表名称 */
	protected static String ORG_TABLE_NAME = null;
	/** 用户表名称 */
	protected static String USER_TABLE_NAME = null;
	/** 组织用户表名称 */
	protected static String ORG_USER_TABLE_NAME = null;
	/** 角色用户表名称 */
	protected static String ROLE_USER_TABLE_NAME = null;
	/** 组织角色表名称 */
	protected static String ORG_ROLE_TABLE_NAME = null;
	/** 系统表名称 */
	protected static String SYSTEM_TABLE_NAME = null;
	/** 系统资源表名称 */
	protected static String SYSTEM_RES_TABLE_NAME = null;
	/** 系统角色表名称 */
	protected static String SYSTEM_ROLE_TABLE_NAME = null;
	/** 资源表名称 */
	protected static String RESOURCE_TABLE_NAME = null;
	/** 角色资源表 */
	protected static String ROLE_RES_TABLE_NAME = null;
	/** 角色表名 */
	protected static String ROLE_TABLE_NAME = null;

	static{
		// 读取排序配置
		ORDER_BY_SHOWORDER = commonSql_Properties.getProperty("ORDER_BY_SHOWORDER");
		// 查询总数配置
		COUNT = commonSql_Properties.getProperty("COUNT");
		
		// 读取字段名配置
		ORG_TYPE_COLUMNS_NAME = commonSql_Properties.getProperty("ORG_TYPE_COLUMNS_NAME");
		ORG_COLUMNS_NAME = commonSql_Properties.getProperty("ORG_COLUMNS_NAME");
		ORG_USER_COLUMNS_NAME = commonSql_Properties.getProperty("ORG_USER_COLUMNS_NAME");
		ROLE_USER_COLUMNS_NAME = commonSql_Properties.getProperty("ROLE_USER_COLUMNS_NAME");
		ORG_ROLE_COLUMNS_NAME = commonSql_Properties.getProperty("ORG_ROLE_COLUMNS_NAME");
		SYSTEM_COLUMNS_NAME = commonSql_Properties.getProperty("SYSTEM_COLUMNS_NAME");
		SYSTEM_RES_COLUMNS_NAME = commonSql_Properties.getProperty("SYSTEM_RES_COLUMNS_NAME");
		SYSTEM_ROLE_COLUMNS_NAME = commonSql_Properties.getProperty("SYSTEM_ROLE_COLUMNS_NAME");
		RESOURCE_COLUMNS_NAME = commonSql_Properties.getProperty("RESOURCE_COLUMNS_NAME");
		ROLE_RES_COLUMNS_NAME = commonSql_Properties.getProperty("ROLE_RES_COLUMNS_NAME");
		ROLE_COLUMNS_NAME = commonSql_Properties.getProperty("ROLE_COLUMNS_NAME");

		// 读取表名配置
		ORG_TABLE_NAME = commonSql_Properties.getProperty("ORG_TABLE_NAME");
		ORG_TYPE_TABLE_NAME = commonSql_Properties.getProperty("ORG_TYPE_TABLE_NAME");
		USER_TABLE_NAME = commonSql_Properties.getProperty("USER_TABLE_NAME");
		ORG_USER_TABLE_NAME = commonSql_Properties.getProperty("ORG_USER_TABLE_NAME");
		ROLE_USER_TABLE_NAME = commonSql_Properties.getProperty("ROLE_USER_TABLE_NAME");
		ORG_ROLE_TABLE_NAME = commonSql_Properties.getProperty("ORG_ROLE_TABLE_NAME");
		SYSTEM_TABLE_NAME = commonSql_Properties.getProperty("SYSTEM_TABLE_NAME");
		SYSTEM_RES_TABLE_NAME = commonSql_Properties.getProperty("SYSTEM_RES_TABLE_NAME");
		SYSTEM_ROLE_TABLE_NAME = commonSql_Properties.getProperty("SYSTEM_ROLE_TABLE_NAME");
		RESOURCE_TABLE_NAME = commonSql_Properties.getProperty("RESOURCE_TABLE_NAME");
		ROLE_RES_TABLE_NAME = commonSql_Properties.getProperty("ROLE_RES_TABLE_NAME");
		ROLE_TABLE_NAME = commonSql_Properties.getProperty("ROLE_TABLE_NAME");

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
