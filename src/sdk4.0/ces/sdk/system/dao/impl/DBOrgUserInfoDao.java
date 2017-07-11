package ces.sdk.system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;
import ces.sdk.util.StringUtil;

public class DBOrgUserInfoDao extends DBBaseDao implements OrgUserInfoDao{

	/**
	 * 查询参数
	 */
	private static String ORG_USER_SELECT_PARAM = null;
	/**
	 * 更新参数
	 */
	private static String ORG_USER_UPDATE_PARAM = null;

	static {
		String[] columnsNameArray = ORG_USER_COLUMNS_NAME.split(",");
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		ORG_USER_SELECT_PARAM = param[0];
		ORG_USER_UPDATE_PARAM = param[1];
	}

	@Override
	public OrgUserInfo save(OrgUserInfo orgUserInfo) {
		
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardInsertSql(ORG_USER_COLUMNS_NAME, ORG_USER_TABLE_NAME, JdbcUtil.generatorPlaceHolder(orgUserInfo.getClass().getDeclaredFields()).toString());
		
		orgUserInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_USER_COLUMNS_NAME, orgUserInfo,ADD_STATUS));

		return orgUserInfo;
	}


	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ORG_USER_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
		
	}

	@Override
	public int delete(String orgId, String userId, String userType) {
		List<String> condition = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		if(StringUtils.isNotBlank(orgId)){
			condition.add("org_id = ?");
			values.add(orgId);
		}
		if(StringUtils.isNotBlank(userId)){
			condition.add("user_id = ?"); // 
			values.add(userId);
		}
		if(StringUtils.isNotBlank(userType)){
			condition.add("user_type = ?");
			values.add(userType);
		}
		String sql = StandardSQLHelper.standardDeleteSql(ORG_USER_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		return qr.update(sql,values.toArray());
	}


	@Override
	public List<OrgUserInfo> findByCondition(Map<String,String> param) {
		List<String> condition = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		
		if(null!=param&&param.size()!=0){
			String orgId = param.get("orgId");
			String userId = param.get("userId");
			String userType = param.get("userType");
			
			if(StringUtils.isNotBlank(orgId)){
				condition.add("org_id = ?");
				values.add(orgId);
			}
			if(StringUtils.isNotBlank(userId)){
				condition.add("user_id = ?"); // 
				values.add(userId);
			}
			if(StringUtils.isNotBlank(userType)){
				condition.add("user_type = ?");
				values.add(userType);
			}
		} 
		
		String sql = StandardSQLHelper.standardSelectSql(ORG_USER_SELECT_PARAM, ORG_USER_TABLE_NAME,null,condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<OrgUserInfo> orgUserInfo = new ArrayList<OrgUserInfo>();
		orgUserInfo = qr.query(sql, new BeanListHandler<OrgUserInfo>(OrgUserInfo.class),values.toArray());
		
		return orgUserInfo;
	}


	@Override
	public void changeOrgUser(String userId, String orgId, String userType) {
		String sql = "update " + ORG_USER_TABLE_NAME + " set org_id = ? where user_id = ? and user_type = ?"; 
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,orgId,userId,userType);
	}
	

}
