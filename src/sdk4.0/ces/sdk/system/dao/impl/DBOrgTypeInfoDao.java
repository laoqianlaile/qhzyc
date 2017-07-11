package ces.sdk.system.dao.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.OrgTypeInfoDao;
import ces.sdk.system.dbfacade.DBOrgTypeInfoFacade;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;
import ces.sdk.util.StringUtil;

public class DBOrgTypeInfoDao extends DBBaseDao implements OrgTypeInfoDao{

	/** 查询参数 */
	private static String ORG_TYPE_SELECT_PARAM = null;  //包括id 的实体类所有属性    common_sql.properties
	/** 更新参数 */
	private static String ORG_TYPE_UPDATE_PARAM = null;

	
	static{
		
		String[] columnsNameArray = ORG_TYPE_COLUMNS_NAME.split(",");
		
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		ORG_TYPE_SELECT_PARAM = param[0];
		ORG_TYPE_UPDATE_PARAM = param[1];
	}
	
	//分页查询
	@Override
	public List<OrgTypeInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		StringBuilder condition = new StringBuilder();
		condition.append(" where 1=1 ");
		
		//创建一个存储动态sql需要注入的值的集合
		List<Object> values = new ArrayList<Object>();
		if(null!=param&&param.size()!=0){
			String name = param.get("name");
			String parentId = param.get("parentId");
			
			if(StringUtil.isNotBlank(name)){
				condition.append(" and name like ? ");
				values.add("%"+name+"%");
			}
			if(StringUtil.isNotBlank(parentId)){
				condition.append(" and parent_id = ? ");
				values.add(parentId);
			}
		} 
		int begin = (currentPage-1)*pageSize+1;
		int end = currentPage*pageSize;
		values.add(begin);
		values.add(end);
		
		String sql = MessageFormat.format(sql_Properties.getProperty("orgTypeFacade_find"), condition) ;
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgTypeInfo> orgTypes = qr.query(sql, new BeanListHandler<OrgTypeInfo>(OrgTypeInfo.class),values.toArray());
		return orgTypes;
	}

	//分页查询 总数量
	@Override
	public int findTotal(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		
		//创建一个存储动态sql需要注入的值的集合
		List<Object> values = new ArrayList<Object>();
		if(null!=param&&param.size()!=0){
			String name = param.get("name");
			String parentId = param.get("parentId");
			if(StringUtil.isNotBlank(name)){
				condition.add("name like ? ");
				values.add("%"+name+"%");
			}
			if(StringUtil.isNotBlank(parentId)){
				condition.add("parent_id = ? ");
				values.add(parentId);
			}
		} 
		String sql = StandardSQLHelper.standardSelectTotalSql(ORG_TYPE_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(), values.toArray());
		if(result != null){
			return result.intValue();
		}
		return 0;
	}

	//查询子组织级别
	@Override
	public List<OrgTypeInfo> findChildsByParentId(String parentId) {
		String sql = StandardSQLHelper.standardSelectSql(ORG_TYPE_SELECT_PARAM, ORG_TYPE_TABLE_NAME, ORDER_BY_SHOWORDER,"parent_id = ?");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgTypeInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgTypeInfo>(OrgTypeInfo.class),parentId);                   
		
		return orgInfos;
	}

	/* (non-Javadoc)
	 * @see ces.sdk.system.facade.OrgTypeFacade#getByID(java.lang.String)
	 */
	//根据ID查询唯一对象
	@Override
	public OrgTypeInfo findByID(String id) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardSelectSql(ORG_TYPE_SELECT_PARAM, ORG_TYPE_TABLE_NAME,null,"id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		OrgTypeInfo orgType = qr.query(sql, new BeanHandler<OrgTypeInfo>(OrgTypeInfo.class),id);
		
		return orgType;
	}

	//保存实体对象
	@Override
	public OrgTypeInfo save(OrgTypeInfo orgTypeInfo) {
		String sql = StandardSQLHelper.standardInsertSql(ORG_TYPE_COLUMNS_NAME, ORG_TYPE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(orgTypeInfo.getClass().getDeclaredFields()).toString());
		
		orgTypeInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_TYPE_COLUMNS_NAME, orgTypeInfo,ADD_STATUS));
		
		return orgTypeInfo;
	}

	//获取在当前父节点下的最大值
	@Override
	public long findMaxOrderNo(String parentId) {
		String sql = StandardSQLHelper.standardSelectMaxSql("show_order", ORG_TYPE_TABLE_NAME, "parent_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(),parentId);
		if(result != null){
			return result.longValue();
		}
		return -1L;
	}

	//更新实体对象
	@Override
	public OrgTypeInfo update(OrgTypeInfo orgTypeInfo) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardUpdateSql(ORG_TYPE_UPDATE_PARAM, ORG_TYPE_TABLE_NAME, "id = ?");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_TYPE_COLUMNS_NAME, orgTypeInfo,UPDATE_STATUS));
		return orgTypeInfo;
	}

	//删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ORG_TYPE_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
	}
	
	//删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	@Override
	public void delete(OrgTypeInfo orgTypeInfo) {
		if(StringUtils.isNotBlank(orgTypeInfo.getId())){
			this.delete(orgTypeInfo.getId());
		}
	}

	//删除组织级别 
	@Override
	public void delete(List<OrgTypeInfo> orgTypeInfos) {
		StringBuilder ids = new StringBuilder();
		for (OrgTypeInfo orgTypeInfo : orgTypeInfos) {
			ids.append(orgTypeInfo.getId()).append(",");
		}
		ids.substring(0, ids.length()-1);
		this.delete(ids.toString());
	}

	//根据条件查询
	@Override
	public List<OrgTypeInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		//创建一个存储动态sql需要注入的值的集合
		List<Object> values = new ArrayList<Object>();
		
		if(null!=param&&param.size()!=0){
			String name = param.get("name");
			String parentId = param.get("parentId");
			String code = param.get("code");
			
			if(StringUtil.isNotBlank(name)){
				condition.add("name = ? ");
				values.add(name);
			}
			if(StringUtil.isNotBlank(parentId)){
				condition.add("parent_id = ? ");
				values.add(parentId);
			}
			if(StringUtil.isNotBlank(code)){
				condition.add("code = ? ");
				values.add(code);
			}
		} 
		String sql = StandardSQLHelper.standardSelectSql(ORG_TYPE_SELECT_PARAM, ORG_TYPE_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgTypeInfo> orgTypes = qr.query(sql, new BeanListHandler<OrgTypeInfo>(OrgTypeInfo.class), values.toArray());

		return orgTypes;
	}

	@Override
	public List<OrgTypeInfo> findAll() {
		String sql = StandardSQLHelper.standardSelectSql(ORG_TYPE_SELECT_PARAM, ORG_TYPE_TABLE_NAME, ORDER_BY_SHOWORDER, "");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgTypeInfo> orgTypes = qr.query(sql, new BeanListHandler<OrgTypeInfo>(OrgTypeInfo.class));
		return orgTypes;
	}
	
}
