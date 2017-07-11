package ces.sdk.system.dbfacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.dao.OrgRoleInfoDao;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.dao.OrgInfoDao;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.dao.UserInfoDao;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.system.factory.SdkDaoFactory;
import ces.sdk.system.factory.SystemFacadeFactory;
import ces.sdk.util.StringUtil;

public class DBOrgInfoFacade extends BaseFacade implements OrgInfoFacade {

	@Override
	public List<OrgInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createOrgInfoDao().find(param, currentPage, pageSize);
	}

	@Override
	public int findTotal(Map<String, String> param) {
		return SdkDaoFactory.createOrgInfoDao().findTotal(param);
	}

	@Override
	public List<OrgInfo> findChildsByParentId(String parentId) {
		return SdkDaoFactory.createOrgInfoDao().findChildsByParentId(parentId);
	}

	@Override
	public OrgInfo findByID(String id) {
		return SdkDaoFactory.createOrgInfoDao().findByID(id);
	}

	@Override
	public OrgInfo save(OrgInfo orgInfo) {
		return SdkDaoFactory.createOrgInfoDao().save(orgInfo);
	}

	@Override
	public OrgInfo update(OrgInfo orgInfo) {
		return SdkDaoFactory.createOrgInfoDao().update(orgInfo);
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		return SdkDaoFactory.createOrgInfoDao().findMaxOrderNo(parentId);
	}

	/**
	 *  删除组织
	 * */
	@Override
	public void delete(String ids) {
		
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();       //用户  表 通道
		OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();      //用户  表 通道
		OrgInfoDao orgInfoDao = SdkDaoFactory.createOrgInfoDao();          //组织  表 通道
		
		UserInfoFacade userFacade = SystemFacadeFactory.newInstance(false).createUserInfoFacade();
		
		String[] idArray = ids.split(","); // 把每一个id 都分出来
		
		
		for(String orgId : idArray)
		{
			Map<String, String> param = new HashMap<String, String>();
			param.put("orgId", orgId);
			List<OrgUserInfo> orgUserInfos = orgUserInfoDao.findByCondition(param);
			//List<UserInfo> userInfos = userInfoDao.findUsersByOrgId(orgId); //  把这个组织下的用户全部查出来
			for(OrgUserInfo orgUserInfo : orgUserInfos)
			{
				userFacade.delete(orgUserInfo.getUserId(), orgUserInfo.getOrgId(),orgUserInfo.getUserType()); // 本来不在独立用户 那么移交 独立用户  本来就在 独立用户  那么 直接 除名
			}
			orgInfoDao.delete(orgId);   // 删除中间的小组织
		}
		
	}

	@Override
	public void delete(OrgInfo orgInfo) {
		if(StringUtils.isNotBlank(orgInfo.getId())){
			this.delete(orgInfo.getId());
		}
	}

	@Override
	public void delete(List<OrgInfo> orgInfos) {
		StringBuilder ids = new StringBuilder();
		for (OrgInfo orgInfo : orgInfos) {
			ids.append(orgInfo.getId()).append(",");
		}
		ids.substring(0, ids.length()-1);
		this.delete(ids.toString());
	}

	@Override
	public List<OrgInfo> findByCondition(Map<String, String> param) {
		return SdkDaoFactory.createOrgInfoDao().findByCondition(param);
	}

	@Override
	public List<OrgInfo> findAll() {
		return SdkDaoFactory.createOrgInfoDao().findAll();
	}

	@Override
	public boolean hasRole(String orgId, String roleId) {
		int total = SdkDaoFactory.createOrgRoleInfoDao().findTotal(orgId, roleId, "");
		
		return total > 0 ? true : false ;
	}

	@Override
	public void grantRole(String orgId, String systemId, String roleId) {
		OrgRoleInfoDao orgRoleInfoDao = SdkDaoFactory.createOrgRoleInfoDao();
		if(StringUtil.isNotBlank(roleId)){
			orgRoleInfoDao.save(orgId, systemId, roleId);
		}
	}
	
	@Override
	public void unGrantRole(String orgId, String systemId, String roleId){
		OrgRoleInfoDao orgRoleInfoDao = SdkDaoFactory.createOrgRoleInfoDao();
		orgRoleInfoDao.delete(orgId, "", "");
	}

	@Override
	public void grantRoleBatch(String orgId, Map<String, String> roleSystemIds) {
		this.unGrantRole(orgId, "", ""); // 先将旧的权限清除
		if(StringUtil.isNotBlank(orgId) && roleSystemIds !=null && roleSystemIds.size() > 0){
			Set<Entry<String,String>> roleSystemIdsSet = roleSystemIds.entrySet();
			//遍历系统角色, 保存组织,角色, 系统关系
			for (Entry<String, String> roleSystemId : roleSystemIdsSet) {
				String roleId = roleSystemId.getKey();
				String systemId = roleSystemId.getValue();
				this.grantRole(orgId, systemId, roleId);
			}
		}
	}
	@Override
	public void changeOrg(String orgId, String parentId) {
		OrgInfoDao orgInfoDao = SdkDaoFactory.createOrgInfoDao();
		orgInfoDao.updateParentIdById(orgId,parentId);
	}

	@Override
	public List<OrgInfo> findOrgByTypeIdAndUserID(String userID, String orgTypeId) {
		OrgInfoDao orgInfoDao = SdkDaoFactory.createOrgInfoDao();
		List<OrgInfo> orgInfos = null;
		orgInfos = orgInfoDao.findOrgByTypeIdAndUserID(userID, orgTypeId);
		return orgInfos;
	}

	@Override
	public List<OrgUserInfo> getOrgUserByUserId(Map<String, String> param) {
		return SdkDaoFactory.createOrgUserDao().findByCondition(param);
		
	}

	

}