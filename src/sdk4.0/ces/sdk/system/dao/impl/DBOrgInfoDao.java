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

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.OrgInfoDao;
import ces.sdk.system.dbfacade.DBOrgInfoFacade;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.ArrayUtil;
import ces.sdk.util.JdbcUtil;
import ces.sdk.util.StringUtil;

public class DBOrgInfoDao extends DBBaseDao implements OrgInfoDao{

	/** 查询参数 */
	private static String ORG_SELECT_PARAM = null;
	/** 更新参数 */
	private static String ORG_UPDATE_PARAM = null;

	static{
		String[] columnsNameArray = ORG_COLUMNS_NAME.split(",");

		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		ORG_SELECT_PARAM = param[0];
		ORG_UPDATE_PARAM = param[1];
		
	}


	//查询子组织级别
	@Override
	public List<OrgInfo> findChildsByParentId(String parentId) {
		String sql = StandardSQLHelper.standardSelectSql(ORG_SELECT_PARAM, ORG_TABLE_NAME, ORDER_BY_SHOWORDER,"parent_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgInfo>(OrgInfo.class),parentId);
		return orgInfos;
	}

	//根据ID查询唯一对象
	@Override
	public OrgInfo findByID(String id) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardSelectSql(ORG_SELECT_PARAM, ORG_TABLE_NAME,null,"id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		OrgInfo orgInfo = qr.query(sql, new BeanHandler<OrgInfo>(OrgInfo.class),id);
		return orgInfo;
	}

	//分页查询
	@Override
	public List<OrgInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		StringBuilder condition = new StringBuilder();
		condition.append(" where 1=1 ");

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
		
		String sql = MessageFormat.format(sql_Properties.getProperty("orgInfoFacade_find"), condition) ;

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgInfo>(OrgInfo.class),values.toArray());
		return orgInfos;
	}

	//分页查询 总数量
	@Override
	public int findTotal(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
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
		String sql = StandardSQLHelper.standardSelectTotalSql(ORG_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(), values.toArray());
		if(result != null){
			return result.intValue();
		}
		return 0;
	}

	
	//保存实体对象
	@Override
	public OrgInfo save(OrgInfo orgInfo) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardInsertSql(ORG_COLUMNS_NAME, ORG_TABLE_NAME, JdbcUtil.generatorPlaceHolder(orgInfo.getClass().getDeclaredFields()).toString());
		
		orgInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_COLUMNS_NAME, orgInfo,ADD_STATUS));
		
		return orgInfo;
	}


	//更新实体对象
	@Override
	public OrgInfo update(OrgInfo orgInfo) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardUpdateSql(ORG_UPDATE_PARAM, ORG_TABLE_NAME, "id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_COLUMNS_NAME, orgInfo,UPDATE_STATUS));
		
		return orgInfo;
	}

	//获取在当前父节点下的最大值
	@Override
	public long findMaxOrderNo(String parentId) {
		String sql = StandardSQLHelper.standardSelectMaxSql("show_order", ORG_TABLE_NAME, "parent_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(),parentId);
		if(result != null){
			return result.longValue();
		}
		return -1L;
	}

	//删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ORG_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
	}

	//根据条件查询
	@Override
	public List<OrgInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
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
		String sql = StandardSQLHelper.standardSelectSql(ORG_SELECT_PARAM, ORG_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgInfo>(OrgInfo.class), values.toArray());
		return orgInfos;
	}
	
	//查询全部
	@Override
	public List<OrgInfo> findAll() {
		String sql = StandardSQLHelper.standardSelectSql(ORG_SELECT_PARAM, ORG_TABLE_NAME, ORDER_BY_SHOWORDER);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgInfo>(OrgInfo.class));
		return orgInfos;
	}

	@Override
	public void updateParentIdById(String orgId, String parentId) {
		String sql = StandardSQLHelper.standardUpdateSql("parent_id = ?", ORG_TABLE_NAME, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,parentId,orgId);
	}

	@Override
	public List<OrgInfo> findOrgByTypeIdAndUserID(String userID, String orgTypeId) {
		//本段代码的功能是给数据表字段加上前缀
		
		String[] selectParamArray = ORG_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "t_org." + selectParamArray[i];
		}
		
		String selectParam = ArrayUtil.toString(selectParamArray);
//		String sql = "select " + ORG_SELECT_PARAM + " from "+ ORG_TABLE_NAME + " where id = (select org_id from "+ORG_USER_TABLE_NAME +" where user_id = ?) and ORG_TYPE_ID = ?";
		String sql = "select "+selectParam  + " from " + ORG_USER_TABLE_NAME + "  t_org_user "
				+ "inner join " + ORG_TABLE_NAME + " t_org on t_org.id = t_org_user.org_id "
				+ "inner join " + ORG_TYPE_TABLE_NAME + " t_org_type on t_org_type.id = t_org.org_type_id "
				+ "where t_org_user.user_id = ? and t_org_user.user_type = '0' and t_org_type.id = ? ";
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgInfo> orgInfos = qr.query(sql, new BeanListHandler<OrgInfo>(OrgInfo.class) , userID , orgTypeId);
		return orgInfos;
	}

}
