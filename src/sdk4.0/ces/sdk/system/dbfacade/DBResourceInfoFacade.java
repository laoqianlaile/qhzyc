package ces.sdk.system.dbfacade;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.SystemResInfo;
import ces.sdk.system.dao.ResourceInfoDao;
import ces.sdk.system.dao.RoleResInfoDao;
import ces.sdk.system.dao.SystemResInfoDao;
import ces.sdk.system.dao.UserInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.factory.SdkDaoFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBResourceInfoFacade extends BaseFacade implements ResourceInfoFacade {

	@Override
	public ResourceInfo findByID(String id) {
		return SdkDaoFactory.createResourceInfoDao().findByID(id);
	}

	@Override
	public int findTotal(Map<String, String> param) {
		return SdkDaoFactory.createResourceInfoDao().findTotal(param);
	}

	@Override
	public List<ResourceInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createResourceInfoDao().find(param, currentPage, pageSize);
	}

	@Override
	public void delete(String id) {
		SdkDaoFactory.createSystemResInfoDao().delete(id, SystemResInfoDao.SystemResIdType.RES);
		SdkDaoFactory.createRoleResInfoDao().delete(id, RoleResInfoDao.RoleResIdType.RES);
		SdkDaoFactory.createResourceInfoDao().delete(id);
	}

	@Override
	public ResourceInfo save(ResourceInfo resourceInfo) {
		return SdkDaoFactory.createResourceInfoDao().save(resourceInfo);
	}

	@Override
	public ResourceInfo save(ResourceInfo resourceInfo, String systemId) {
		ResourceInfo info = SdkDaoFactory.createResourceInfoDao().save(resourceInfo);
		SystemResInfo systemResInfo = new SystemResInfo();
		systemResInfo.setResourceId(info.getId());
		systemResInfo.setSystemId(systemId);
		SdkDaoFactory.createSystemResInfoDao().save(systemResInfo);
		return info;
	}

	@Override
	public List<ResourceInfo> findChildsByParentId(String parentId) {
		return SdkDaoFactory.createResourceInfoDao().findChildsByParentId(parentId);
	}

	@Override
	public ResourceInfo update(ResourceInfo resourceInfo) {
		return SdkDaoFactory.createResourceInfoDao().update(resourceInfo);
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		return SdkDaoFactory.createResourceInfoDao().findMaxOrderNo(parentId);
	}

	@Override
	public List<ResourceInfo> findByCondition(Map<String, String> param) {
		return SdkDaoFactory.createResourceInfoDao().findByCondition(param);
	}

	@Override
	public List<ResourceInfo> findResInfosByRoleId(String roleId) throws SystemFacadeException {
//		List<ResourceInfo> resources = new ArrayList<ResourceInfo>();
//
//		ResourceInfo resourceInfo = new ResourceInfo();
//		resourceInfo.setId("-1");
//		resourceInfo.setUrl("/*");
//		resourceInfo.setName("系统管理平台4.0");
//		resourceInfo.setComments("03");
//		resources.add(resourceInfo);
//		return resources;
		return SdkDaoFactory.createResourceInfoDao().findResInfosByRoleId(roleId);
	}

	@Override
	public int findResourcesTotalBySystemId(String systemId, Map<String, String> param) {
		return SdkDaoFactory.createResourceInfoDao().findResourcesTotalBySystemId(systemId, param);
	}

	@Override
	public List<ResourceInfo> findResourcesPageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createResourceInfoDao().findResourcesPageBySystemId(systemId, param, currentPage, pageSize);
	}

	@Override
	public List<ResourceInfo> findResourcesBySystemId(String systemId, Map<String, String> param) {
		return SdkDaoFactory.createResourceInfoDao().findResourcesBySystemId(systemId, param);
	}

	@Override
	public String findSystemIdByResource(String resourceId) {
		return SdkDaoFactory.createResourceInfoDao().findSystemIdByResource(resourceId);
	}

	@Override
	public boolean hasChild(String resourceId) {
		List<ResourceInfo> childResources = SdkDaoFactory.createResourceInfoDao().findChildsByParentId(resourceId);
		return !childResources.isEmpty();
	}

	@Override
	public void moveResource(String newPid, String[] resIds) {
		SdkDaoFactory.createResourceInfoDao().moveResource(newPid, resIds);
	}

	@Override
	public List<ResourceInfo> findResourcesByUserIdAndSystemId(String userId, String systemId) {
		return SdkDaoFactory.createResourceInfoDao().findResourcesByUserIdAndSystemId(userId,systemId);
	}
	

	@Override
	public List<ResourceInfo> findResourcesPageByRoleId(String roleId, Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createResourceInfoDao().findResourcesPageByRoleId(roleId, param, currentPage, pageSize);
	}

	@Override
	public List<ResourceInfo> getResrouceByUserId(String userId) throws SystemFacadeException{
		UserInfoDao userInfoDao = SdkDaoFactory.createUserInfoDao();
		List<String> roleIdList = userInfoDao.getRoleByUserId(userId);
		List<ResourceInfo> resultList = new ArrayList<ResourceInfo>();

		for(String roleId:roleIdList){
			List<ResourceInfo> resourceInfoList = SdkDaoFactory.createResourceInfoDao().findResInfosByRoleId(roleId);
			resultList = joinList(resourceInfoList,resultList);
		}
		return resultList;
	}

	@Override
	public List<ResourceInfo> findResourceByUserIdAndSystemCode(String userId, String systemCode) {
		return SdkDaoFactory.createResourceInfoDao().findResourceByUserIdAndSystemCode(userId,systemCode);
	}

	@Override
	public List<ResourceInfo> findResourceByUserIdAndResKey(String userId, String resKey) {
		ResourceInfoDao resrouceInfoDao = SdkDaoFactory.createResourceInfoDao();
		ResourceInfo resourceInfo = resrouceInfoDao.findByKey(resKey);
		return resrouceInfoDao.findResourceByUserIdAndParentId(userId,resourceInfo.getId());
	}

	@Override
	public void save(List<ResourceInfo> resourceInfos, String systemId) {
		for (ResourceInfo resourceInfo : resourceInfos) {
			save(resourceInfo, systemId);
		}
	}

	@Override
	public ResourceInfo findByKey(String resourceKey) {
		return SdkDaoFactory.createResourceInfoDao().findByKey(resourceKey);
	}

	//list合并去重
	private List<ResourceInfo> joinList(List<ResourceInfo> list1,List<ResourceInfo> list2){
		boolean flag = true;
		for(ResourceInfo resourceInfo1:list1){
			for(ResourceInfo resourceInfo2:list2){
				if((resourceInfo1.getId()).equals(resourceInfo2.getId())){
					flag = false;
					break;
				}
			}
			if(flag){
				list2.add(resourceInfo1);
			}
			flag = true;
		}
		return list2;
	}
}