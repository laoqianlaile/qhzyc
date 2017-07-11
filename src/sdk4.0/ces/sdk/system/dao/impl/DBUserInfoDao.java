package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.common.CommonConst;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.UserInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBUserInfoDao extends DBBaseDao implements UserInfoDao{

	private static String USER_COLUMNS_NAME = null;
	/** 查询参数 */
	private static String USER_SELECT_PARAM = null;
	/** 更新参数 */
	private static String USER_UPDATE_PARAM = null;

	
	static{
		USER_COLUMNS_NAME = commonSql_Properties.getProperty("USER_COLUMNS_NAME");
		
		String[] columnsNameArray = USER_COLUMNS_NAME.split(",");
		
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		USER_SELECT_PARAM = param[0];
		USER_UPDATE_PARAM = param[1];
	}


	@Override
	public List<UserInfo> find(Map<String, String> param, int currentPage,
			int pageSize) {
		StringBuilder condition = new StringBuilder();
		condition.append(" where 1=1 ");
		List<Object> values = new ArrayList<Object>();
		if(null!=param&&param.size()!=0){
			
			String name = param.get("name");
			String orgId = param.get("parentId");
			String belongOrgId = param.get("belongOrgId");
			
			if(StringUtil.isNotBlank(name)){
				condition.append(" and u.name like ? ");
				values.add("%"+name+"%");
			}
			if(StringUtil.isNotBlank(orgId)){
				condition.append(" and ou.org_id = ? ");
				values.add(orgId);
			}

			if(StringUtil.isNotBlank(belongOrgId)){
				condition.append(" and u.belong_org_id = ? ");
				values.add(belongOrgId);
			}
			
		} 
		condition.append(" and u.status = '"+CommonConst.UserInfo.ONLINE+"' ");
		
		int begin = (currentPage-1)*pageSize+1;
		int end = currentPage*pageSize;
		values.add(begin);
		values.add(end);
		
		String sql = MessageFormat.format(sql_Properties.getProperty("userInfoFacade_find"), condition) ;

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql, new BeanListHandler<UserInfo>(UserInfo.class),values.toArray());
		
		return userInfos;
	}

	@Override
	public int findTotal(Map<String, String> param) {
		String fromTable = " "+USER_TABLE_NAME+" u inner join "+ORG_USER_TABLE_NAME+" ou on u.id = ou.user_id";
		
		List<String> condition = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		if(null!=param&&param.size()!=0){
			String name = param.get("name");
			String orgId = param.get("parentId");
			if(StringUtil.isNotBlank(name)){
				condition.add("u.name like ? ");
				values.add("%"+name+"%");
			}
			if(StringUtil.isNotBlank(orgId)){
				condition.add("ou.org_id = ? ");
				values.add(orgId);
			}
		} 
		condition.add("u.status = '"+CommonConst.UserInfo.ONLINE+"'");

		String sql = StandardSQLHelper.standardSelectTotalSql(fromTable, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		Long result = qr.query(sql.toString(), new ScalarHandler<Long>(), values.toArray());
		if(result != null){
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<UserInfo> findUsersByOrgId(String orgId) {
		
		String[] selectParamArray = USER_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "u."+selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " "+USER_TABLE_NAME+" u inner join "+ORG_USER_TABLE_NAME+" ou on u.id = ou.user_id";
		
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, "ou.org_id = ? and u.status =\'0\'");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql, new BeanListHandler<UserInfo>(UserInfo.class), orgId);
		
		return userInfos;
	}

	@Override
	public UserInfo findByID(String id) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardSelectSql(USER_SELECT_PARAM, USER_TABLE_NAME,null,"id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		UserInfo userInfo = qr.query(sql, new BeanHandler<UserInfo>(UserInfo.class),id);
		return userInfo;
	}

	@Override
	public UserInfo findByLoginName(String loginName) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardSelectSql(USER_SELECT_PARAM, USER_TABLE_NAME,null,"login_name = ? and status = '"+CommonConst.UserInfo.ONLINE+"'");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		UserInfo userInfo = qr.query(sql, new BeanHandler<UserInfo>(UserInfo.class),loginName);
		
		return userInfo;
	}

	@Override
	public UserInfo save(UserInfo userInfo) {
		String sql = StandardSQLHelper.standardInsertSql(USER_COLUMNS_NAME, USER_TABLE_NAME, JdbcUtil.generatorPlaceHolder(BeanUtil.getDeclaredFields(UserInfo.class)).toString());
        
		userInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(USER_COLUMNS_NAME, userInfo,ADD_STATUS));

		return userInfo;
	}

	@Override
	public UserInfo update(UserInfo userInfo) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardUpdateSql(USER_UPDATE_PARAM, USER_TABLE_NAME, "id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(USER_COLUMNS_NAME, userInfo,UPDATE_STATUS));
		
		return userInfo;
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		String sql = StandardSQLHelper.standardSelectMaxSql("show_order", USER_TABLE_NAME, "status = '"+CommonConst.UserInfo.ONLINE+"'");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>());
		if(result != null){
			return result.longValue();
		}
		return -1L;
	}

	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = "update "+USER_TABLE_NAME+" set status = '"+CommonConst.UserInfo.OFFLINE+ "' where id in " + JdbcUtil.generatorPlaceHolder(idsArray);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,id);
	}


	@Override
	public List<UserInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		
		if(null!=param&&param.size()!=0){
			String loginName = param.get("loginName");
			if(StringUtil.isNotBlank(loginName)){
				condition.add("login_name = ? ");
				values.add(loginName);
			}
		} 
		
		String sql = StandardSQLHelper.standardSelectSql(USER_SELECT_PARAM, USER_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql, new BeanListHandler<UserInfo>(UserInfo.class), values.toArray());

		return userInfos;
	}

	@Override
	public List<UserInfo> findAll() {
		String sql = StandardSQLHelper.standardSelectSql(USER_SELECT_PARAM, USER_TABLE_NAME, ORDER_BY_SHOWORDER,"status = '"+CommonConst.UserInfo.ONLINE+"'");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql, new BeanListHandler<UserInfo>(UserInfo.class));

		return userInfos;
	}

	@Override
	public void resetUserPasswd(String userId) {
		String sql = StandardSQLHelper.standardUpdateSql("password = ?", USER_TABLE_NAME, "id = ?");
		String password = (new MD5()).getMD5ofStr(CommonConst.UserInfo.DEFAULT_PASSWORD).toLowerCase();
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, password,userId);
		sql = null;
		password = null;
		
	}

	@Override
	public int updateUserPasswd(String loginName, String oldPassword, String newPassword) {
    	String sql = StandardSQLHelper.standardUpdateSql("password = ?", USER_TABLE_NAME, "login_name = ?");
    	
    	SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
    	qr.update(sql, newPassword,loginName);
		
    	sql = null;
        return 0;
	}

	public List<UserInfo> findUserInfosByRoleId(String roleId) {
		String sql = "select " + USER_SELECT_PARAM + "  from "+ USER_TABLE_NAME + " where id in (select user_id  from " + ROLE_USER_TABLE_NAME + " where role_id =  ? ) " ;
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql, new BeanListHandler<UserInfo>(UserInfo.class),roleId);
		return userInfos;
	}

	@Override
	public List<String> getRoleByUserId(String userId) {
		String sql = StandardSQLHelper.standardSelectSql("ROLE_ID", ROLE_USER_TABLE_NAME,null,"USER_ID = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<String> roleIdList = qr.query(sql,new ColumnListHandler<String>(),userId);
		return roleIdList;
	}

}
