package ces.sdk.system.dbfacade;

import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.RoleResInfo;
import ces.sdk.system.bean.RoleUserInfo;
import ces.sdk.system.bean.SystemRoleInfo;
import ces.sdk.system.dao.OrgRoleInfoDao;
import ces.sdk.system.dao.RoleResInfoDao;
import ces.sdk.system.dao.RoleUserInfoDao;
import ces.sdk.system.dao.SystemRoleInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.factory.SdkDaoFactory;

import java.util.*;

/**
 * 角色操作
 *
 */
public class DBRoleInfoFacade extends BaseFacade implements RoleInfoFacade {

	@Override
	public long findMaxOrderNo(String systemId) {
		return SdkDaoFactory.createRoleInfoDao().findMaxOrderNo(systemId);
	}

	@Override
	public List<RoleInfo> findAllRoleInfos() {
		return SdkDaoFactory.createRoleInfoDao().findByCondition(null);
	}

	/**
	 * 本方法 方法体有误 无法进行测试
	 * discovered by wj
	 * */
	@Override
	public List<RoleInfo> findRolesBySysCode(String sysCode) {
		return findRolesBySystemId(sysCode, null);
	}

	@Override
	public List<RoleInfo> findRolesBySystemId(String systemId, Map<String, String> param) {
		return SdkDaoFactory.createRoleInfoDao().findRolesBySystemId(systemId, param);
	}

	@Override
	public List<RoleInfo> findRoleInfosByUserId(String userId, String sysCode) throws SystemFacadeException {
		return SdkDaoFactory.createRoleInfoDao().findRoleInfosByUserId(userId, sysCode);
	}

	@Override
	public List<RoleInfo> findRoleInfosByUserId(String userId) throws SystemFacadeException {
		return SdkDaoFactory.createRoleInfoDao().findRoleInfosByUserId(userId);
	}

	//TODO 没有完成
	@Override
	public List<RoleInfo> find(Map<String, String> paramMap, int currentPage, int pageSize) {
		List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId("1");
		roleInfo.setName("超级管理员");
		roleInfo.setRoleKey("superrole");
		roleInfo.setComments("超级管理员");
		roleInfos.add(roleInfo);
		return roleInfos;
	}

	@Override
	public List<RoleInfo> findByCondition(Map<String, String> param) {
		return SdkDaoFactory.createRoleInfoDao().findByCondition(param);
	}

	@Override
	public RoleInfo save(RoleInfo roleInfo, String systemId) {
		RoleInfo info = SdkDaoFactory.createRoleInfoDao().save(roleInfo);
		SystemRoleInfo systemRoleInfo = new SystemRoleInfo();
		systemRoleInfo.setRoleId(info.getId());
		systemRoleInfo.setSystemId(systemId);
		SdkDaoFactory.createSystemRoleInfoDao().save(systemRoleInfo);
		return info;
	}

	@Override
	public RoleInfo findByID(String id) {
		return SdkDaoFactory.createRoleInfoDao().findByID(id);
	}

	@Override
	public RoleInfo update(RoleInfo roleInfo) {
		return SdkDaoFactory.createRoleInfoDao().update(roleInfo);
	}

	@Override
	public void delete(String id) {
		//删除角色用户关联
		for (String roleId : id.split(",")) {
			SdkDaoFactory.createRoleUserDao().delete(roleId, null, null,null);
		}
		//删除角色资源关联表
		SdkDaoFactory.createRoleResInfoDao().delete(id, RoleResInfoDao.RoleResIdType.ROLE);
		//删除系统角色关联表
		SdkDaoFactory.createSystemRoleInfoDao().delete(id, SystemRoleInfoDao.SystemRoleIdType.ROLE);
		//删除角色
		SdkDaoFactory.createRoleInfoDao().delete(id);
	}

	@Override
	public int findTotal(String systemId, Map<String, String> param) {
		return SdkDaoFactory.createRoleInfoDao().findTotal(systemId, param);
	}

	@Override
	public List<RoleInfo> findRolePageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createRoleInfoDao().findRolePageBySystemId(systemId, param, currentPage, pageSize);
	}

	@Override
	public String findSystemIdByRole(String roleId) {
		return SdkDaoFactory.createRoleInfoDao().findSystemIdByRole(roleId);
	}

	@Override
	public int findResourceTotal(String roleId, Map<String, String> param) {
		return SdkDaoFactory.createRoleInfoDao().findResourceTotal(roleId, param);
	}

	@Override
	public void grantResource(String roleId, String resIds) {
		SdkDaoFactory.createRoleResInfoDao().delete(roleId, RoleResInfoDao.RoleResIdType.ROLE); //把原来的角色和资源断开关系
		for (String resId : resIds.split(",")) {
			RoleResInfo roleResInfo = new RoleResInfo();
			roleResInfo.setRoleId(roleId);
			roleResInfo.setResourceId(resId);
			SdkDaoFactory.createRoleResInfoDao().save(roleResInfo);
		}
	}

	@Override
	public int findUserInfosByRoleIdTotal(Map<String, String> param) {
		return SdkDaoFactory.createRoleUserDao().findByConditionTotal(param);
//		return SdkDaoFactory.createRoleInfoDao().findUserInfosByRoleIdTotal(param);
	}

	@Override
	public List<RoleUserInfo> findUserInfosByRoleIdPage(Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createRoleUserDao().findByConditionPage(param, currentPage, pageSize);
//		return SdkDaoFactory.createRoleInfoDao().findUserInfosByRoleIdPage(roleId, param, currentPage, pageSize);
	}

	@Override
	public void authorizeUser(String roleId, List<Map<String, String>> userAndOrgIds, String isTempAccredit, String dateStart, String dateEnd) {
		for (Map<String, String> userAndOrgId : userAndOrgIds) {
			authorizeUser(roleId, userAndOrgId.get("userId"),userAndOrgId.get("orgId"), isTempAccredit, dateStart, dateEnd);
		}
	}

	@Override
	public void authorizeUser(String roleId, String userId,String orgId, String isTempAccredit, String dateStart, String dateEnd) {
		String systemId = SdkDaoFactory.createRoleInfoDao().findSystemIdByRole(roleId);
		SdkDaoFactory.createRoleUserDao().delete(roleId, userId, null,orgId);
		RoleUserInfo roleUserInfo = new RoleUserInfo();
		roleUserInfo.setRoleId(roleId);
		roleUserInfo.setUserId(userId);
		roleUserInfo.setOrgId(orgId);
		roleUserInfo.setIsTempAccredit(isTempAccredit);
		roleUserInfo.setDateStart(dateStart);
		roleUserInfo.setDateEnd(dateEnd);
		roleUserInfo.setSystemId(systemId);
		SdkDaoFactory.createRoleUserDao().save(roleUserInfo);
	}

	/**
	 * 本方法有问题  本方法涉及ROLE_USER_TABLE 但是 该表中不含有 orgid 字段逻辑上是不成立的
	 * */
	@Override
	public boolean hasRole(String roleId, String userId, String orgId) {
		return SdkDaoFactory.createRoleInfoDao().hasRole(roleId, userId, orgId);
	}

	/**
	 * 本方法有问题  本方法涉及ROLE_USER_TABLE 但是 该表中不含有 orgid 字段逻辑上是不成立的
	 * */
	@Override
	public void unAuthorizeUser(String roleId, List<Map<String,String>> userAndOrgIds) {
		for (Map<String,String> userAndOrgId : userAndOrgIds) {
			unAuthorizeUser(roleId, userAndOrgId.get("userId"), userAndOrgId.get("orgId"));
		}
	}

	@Override
	public void unAuthorizeUser(String roleId, String userId, String orgId) {
		SdkDaoFactory.createRoleUserDao().delete(roleId, userId,"",orgId);
	}

	@Override
	public boolean hasResource(String roleId, String resourceId) {
		return SdkDaoFactory.createRoleInfoDao().hasResource(roleId, resourceId);
	}

	@Override
	public void removeResource(String roleId, String id) {
		SdkDaoFactory.createRoleInfoDao().removeResource(roleId, id);
	}

	@Override
	public Map<String, Set<String>> findCheckedSystemAndRoleByOrgId(String orgId) {
		OrgRoleInfoDao orgRoleInfoDao = SdkDaoFactory.createOrgRoleInfoDao();
		Set<String> systemIds = orgRoleInfoDao.findSystemIdsByOrgId(orgId);
		Set<String> roleIds = orgRoleInfoDao.findRoleIdsByOrgId(orgId);
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();
		map.put("systemIds", systemIds);
		map.put("roleIds", roleIds);
		return map;
	}

	@Override
	public List<RoleInfo> findRolesBysystemIdAndOrgId(String systemId, String parentOrgId) {
		return SdkDaoFactory.createRoleInfoDao().findRolesBysystemIdAndOrgId(systemId,parentOrgId);
	}

	@Override
	public Map<String, Set<String>> findCheckedSystemAndRoleByUserId(String userId) {
		RoleUserInfoDao roleUserInfoDao = SdkDaoFactory.createRoleUserDao();
		//OrgRoleInfoDao orgRoleInfoDao = SdkDaoFactory.createOrgRoleInfoDao();
		Set<String> systemIds = roleUserInfoDao.findSystemIdsByUserId(userId);
		Set<String> roleIds = roleUserInfoDao.findRoleIdsByUserId(userId);
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();
		map.put("systemIds", systemIds);
		map.put("roleIds", roleIds);
		return map;
	}

}