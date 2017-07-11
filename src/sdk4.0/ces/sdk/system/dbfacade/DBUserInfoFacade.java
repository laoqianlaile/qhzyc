package ces.sdk.system.dbfacade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.ces.xarch.plugins.common.global.Constants;
import com.sun.org.apache.xpath.internal.operations.And;

import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.bean.RoleUserInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.common.CommonConst;
import ces.sdk.system.dao.OrgRoleInfoDao;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.dao.RoleInfoDao;
import ces.sdk.system.dao.RoleUserInfoDao;
import ces.sdk.system.dao.UserInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.system.factory.SdkDaoFactory;
import ces.sdk.util.MD5;
import ces.sdk.util.StringUtil;

public class DBUserInfoFacade extends BaseFacade implements UserInfoFacade {

	@Override
	public List<UserInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.find(param, currentPage, pageSize);
	}

	@Override
	public int findTotal(Map<String, String> param) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findTotal(param);
	}

	@Override
	public List<UserInfo> findUsersByOrgId(String orgId) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findUsersByOrgId(orgId);
	}

	@Override
	public UserInfo findByID(String id) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findByID(id);
	}

	@Override
	public UserInfo findByLoginName(String loginName) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findByLoginName(loginName);
	}

	@Override
		//保存用户信息
	public UserInfo save(UserInfo userInfo,String orgId,String roleId) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		//密码加密处理
		String password = userInfo.getPassword();
        if(StringUtils.isNotBlank(password)){
        	password = (new MD5()).getMD5ofStr(password).toLowerCase();
        }else{
        	password = (new MD5()).getMD5ofStr(CommonConst.UserInfo.DEFAULT_PASSWORD).toLowerCase();
        }
        userInfo.setPassword(password);
		userInfo.setStatus(CommonConst.UserInfo.ONLINE);
		userInfo = userInfoDao.save(userInfo);
		
		//保存用户组织关系信息
		OrgUserInfo orgUser = new OrgUserInfo();
		orgUser.setOrgId(orgId);
		orgUser.setUserId(userInfo.getId());
		orgUser.setUserType(CommonConst.UserInfo.FULL_TIME); //新建用户默认是专职的
		OrgUserInfoDao orgUserDao = SdkDaoFactory.createOrgUserDao();
		orgUserDao.save(orgUser);
		
		//新建用户绑定默认角色
		RoleUserInfo roleUser = new RoleUserInfo();
		roleUser.setRoleId(CommonConst.RoleInfo.DEFAULT_ROLE); //新建用户默认给予默认角色
		roleUser.setUserId(userInfo.getId());
		roleUser.setSystemId(CommonConst.System.AUTHSYSTEM);
		RoleUserInfoDao roleUserDao = SdkDaoFactory.createRoleUserDao();
		roleUserDao.save(roleUser);
		
		//保存用户自定义角色关系信息
		if(StringUtil.isNotBlank(roleId)){
			roleUser = null;
			roleUser = new RoleUserInfo();
			roleUser.setRoleId(roleId); 
			roleUser.setUserId(userInfo.getId());
			roleUser.setSystemId(CommonConst.System.AUTHSYSTEM);
			roleUserDao = SdkDaoFactory.createRoleUserDao();
			roleUserDao.save(roleUser);
		}
		
		return userInfo;
	}

	@Override
	public UserInfo update(UserInfo userInfo) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		String password = this.findByID(userInfo.getId()).getPassword();
		userInfo.setPassword(password);
		return userInfoDao.update(userInfo);
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findMaxOrderNo(parentId);
	}

	@Override
	public void delete(String id) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		OrgUserInfoDao orgUserDao = SdkDaoFactory.createOrgUserDao();
		RoleUserInfoDao roleUserDao = SdkDaoFactory.createRoleUserDao();
		
		String[] idsArray = id.split(",");
		for (String userId : idsArray) {
			userInfoDao.delete(userId);
			orgUserDao.delete("", userId, "");
			roleUserDao.delete("", userId,"","");
		}
	}

	@Override
	public void delete(UserInfo userInfo) {
		if(StringUtils.isNotBlank(userInfo.getId())){
			this.delete(userInfo.getId());
		}
	}

	@Override
	public void delete(List<UserInfo> userInfos) {
		StringBuilder ids = new StringBuilder();
		for (UserInfo userInfo : userInfos) {
			ids.append(userInfo.getId()).append(",");
		}
		ids.substring(0, ids.length()-1);
		this.delete(ids.toString());
	}

	public void delete(String userId, String orgId, String userType){
		
		OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		RoleUserInfoDao roleUserDao = SdkDaoFactory.createRoleUserDao();
		if(CommonConst.OrgInfo.INDEPENDENT_ORG.equals(orgId)){ //INDEPENDENT_ORG =  1  独立组织
			orgUserInfoDao.delete(orgId, userId, ""); //如果是 “独立用户”的删除 那么就是真的把用户从视野中删除 ， 如果不是 
			userInfoDao.delete(userId);  //数据通道 将该用户在关联表中的记录删除
		} else {
			
			if(CommonConst.UserInfo.FULL_TIME.equals(userType)){ //专职用户删除
				OrgUserInfo orgUserInfo = new OrgUserInfo(); //建立一条新的 关联数据 并且 插入到 关联数据表中 
				orgUserInfo.setOrgId(CommonConst.OrgInfo.INDEPENDENT_ORG); // 使得 与 “独立组织关联在一起”
				orgUserInfo.setUserId(userId);
				orgUserInfo.setUserType(CommonConst.UserInfo.ONLINE);  //ONLINE 专职的意思
				
				orgUserInfoDao.delete(null, userId, null); 
				orgUserInfoDao.save(orgUserInfo);
				
				roleUserDao.delete("", userId,"",""); // 删除角色所关联的 角色
			} else {
				orgUserInfoDao.delete(orgId, userId, userType);
				roleUserDao.delete("", userId,"",orgId); // 删除角色所关联的 角色
			}
		}
		
		
	}
	
	@Override
	public List<UserInfo> findByCondition(Map<String, String> param) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findByCondition(param);
	}

	@Override
	public List<UserInfo> findAll() {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.findAll();
	}

	@Override
	public void unGrantRole(String userId, String systemId, String roleId,String orgId){
		RoleUserInfoDao roleUserDao = SdkDaoFactory.createRoleUserDao();
		roleUserDao.delete("", userId, "",orgId);
	}
	
	@Override
	public void grantRole(String userId, String roleId, String systemId,String orgId) {
		RoleUserInfoDao roleUserDao = SdkDaoFactory.createRoleUserDao();
		
		RoleUserInfo roleUserInfo = new RoleUserInfo();
		roleUserInfo.setRoleId(roleId);
		roleUserInfo.setUserId(userId);
		roleUserInfo.setIsTempAccredit(null);
		roleUserInfo.setDateStart(null);
		roleUserInfo.setDateEnd(null);
		roleUserInfo.setSystemId(systemId);
		roleUserInfo.setOrgId(orgId);
		roleUserDao.save(roleUserInfo);
	}

	@Override
	public void grantRoleBatch(String userId, Map<String, String> roleSystemIds,String orgId) {
		this.unGrantRole(userId, "", "",orgId); // 先将旧的权限清除
		if(StringUtil.isNotBlank(userId) && roleSystemIds !=null && roleSystemIds.size() > 0){
			Set<Entry<String,String>> roleSystemIdsSet = roleSystemIds.entrySet();
			//遍历系统角色, 保存组织,角色, 系统关系
			for (Entry<String, String> roleSystemId : roleSystemIdsSet) {
				String roleId = roleSystemId.getKey();
				String systemId = roleSystemId.getValue();
				this.grantRole(userId, roleId, systemId,orgId);
			}
		}
	}
	
	@Override
	public void parttime(String userId, String addParttimeOrgIds, String removeParttimeOrgIds) {
		if(StringUtil.isNotBlank(userId) && StringUtils.isNotBlank(addParttimeOrgIds)){
			//删除取消兼职的兼职关系和兼职授权
			OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
			RoleUserInfoDao roleUserInfoDao = SdkDaoFactory.createRoleUserDao();
			if(StringUtils.isNotBlank(removeParttimeOrgIds)){
				for (String  orgId : removeParttimeOrgIds.split(",")) {
					roleUserInfoDao.delete(null, userId, null, orgId);
					orgUserInfoDao.delete(orgId, userId, CommonConst.UserInfo.PART_TIME);
				}
			}
			// 保存新的兼职关系
			OrgUserInfo orgUserInfo = null;
			if(StringUtils.isNotBlank(addParttimeOrgIds)){
				for (String  orgId : addParttimeOrgIds.split(",")) {
					orgUserInfo = new OrgUserInfo();
					orgUserInfo.setOrgId(orgId);
					orgUserInfo.setUserId(userId);
					orgUserInfo.setUserType(CommonConst.UserInfo.PART_TIME);
					orgUserInfoDao.save(orgUserInfo);
					orgUserInfo = null;
				}
			}
		}
	}

	@Override
	public void resetUserPasswd(String userId) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		userInfoDao.resetUserPasswd(userId);
	}

	@Override
	public int updateUserPasswd(String loginName, String oldPassword, String newPassword) {
		//验证原密码是否正确
        UserInfo userInfo = this.findByLoginName(loginName);
        if(userInfo == null){
            return 3;
        }
    	if(oldPassword != null && oldPassword.trim().length() > 0){
    		String oldpw = (new MD5()).getMD5ofStr(oldPassword).toLowerCase();
    		if(!userInfo.getPassword().equalsIgnoreCase(oldpw)){ 
    			return 1;
    		}
    	}       
    	
    	newPassword =  (new MD5()).getMD5ofStr(newPassword).toLowerCase();
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		return userInfoDao.updateUserPasswd(loginName, oldPassword, newPassword);
	}

	@Override
	public List<UserInfo> findUserInfosByRoleId(String roleId)
			throws SystemFacadeException {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		List<UserInfo> userInfos = null;
		userInfos = userInfoDao.findUserInfosByRoleId(roleId);
		return userInfos;
	}

	@Override
	public void joinOrg(String userIds, String orgId) {
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
		String[] userIdsArray = userIds.split(",");
		for (String userId : userIdsArray) {
			orgUserInfoDao.changeOrgUser(userId,orgId,CommonConst.UserInfo.FULL_TIME);
		}
	}
	
	

}