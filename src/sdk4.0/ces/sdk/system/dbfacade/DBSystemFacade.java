package ces.sdk.system.dbfacade;

import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.dao.SystemResInfoDao;
import ces.sdk.system.dao.SystemRoleInfoDao;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.factory.SdkDaoFactory;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBSystemFacade extends BaseFacade implements SystemFacade {

	@Override
	public List<SystemInfo> findSystems(Map<String, String> param) {
		return SdkDaoFactory.createSystemInfoDao().findByCondition(param);
	}

	@Override
	public List<SystemInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		return SdkDaoFactory.createSystemInfoDao().find(param, currentPage, pageSize);
	}

	@Override
	public int findTotal(Map<String, String> param) {
		return SdkDaoFactory.createSystemInfoDao().findTotal(param);
	}

	@Override
	public SystemInfo findByID(String id) {
		return SdkDaoFactory.createSystemInfoDao().findByID(id);
	}

	@Override
	public SystemInfo save(SystemInfo systemInfo) {
		return SdkDaoFactory.createSystemInfoDao().save(systemInfo);
	}

	@Override
	public SystemInfo update(SystemInfo systemInfo) {
		return SdkDaoFactory.createSystemInfoDao().update(systemInfo);
	}

	@Override
	public void delete(String id) {
		SdkDaoFactory.createSystemResInfoDao().delete(id, SystemResInfoDao.SystemResIdType.SYSTEM);
		SdkDaoFactory.createSystemRoleInfoDao().delete(id, SystemRoleInfoDao.SystemRoleIdType.SYSTEM);
		SdkDaoFactory.createSystemInfoDao().delete(id);
	}

	@Override
	public void delete(SystemInfo systemInfo) {
		if (StringUtils.isNotBlank(systemInfo.getId())) {
			this.delete(systemInfo.getId());
		}

	}

	@Override
	public void delete(List<SystemInfo> systemInfos) {
		StringBuilder ids = new StringBuilder();
		for (SystemInfo systemInfo : systemInfos) {
			ids.append(systemInfo.getId()).append(",");
		}
		ids.substring(0, ids.length() - 1);
		delete(ids.toString());
	}

	@Override
	public List<SystemInfo> findByCondition(Map<String, String> param) {
		return SdkDaoFactory.createSystemInfoDao().findByCondition(param);
	}

	@Override
	public long findMaxOrderNo() {
		return SdkDaoFactory.createSystemInfoDao().findMaxOrderNo();
	}

	@Override
	public boolean isSystemExists(String systemId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", systemId);
		int total = findTotal(param);
		return total > 0 ? true : false;
	}

	@Override
	public boolean hasResource(String systemId) {
		return SdkDaoFactory.createSystemInfoDao().hasResource(systemId);
	}

	@Override
	public boolean hasRole(String systemId) {
		return SdkDaoFactory.createSystemInfoDao().hasRole(systemId);
	}
	
	public List<SystemInfo> findAll(){
		return SdkDaoFactory.createSystemInfoDao().findAll();
	}

	@Override
	public List<SystemInfo> findSystemsByOrgId(String parentOrgId) {
		return SdkDaoFactory.createSystemInfoDao().findSystemsByOrgId(parentOrgId);
	}

	@Override
	public SystemInfo findSystemsByCode(String code) {
		return SdkDaoFactory.createSystemInfoDao().findSystemsByCode(code);
	}
}
