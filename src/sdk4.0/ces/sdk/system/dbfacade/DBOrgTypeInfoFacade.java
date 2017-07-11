package ces.sdk.system.dbfacade;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.facade.OrgTypeInfoFacade;
import ces.sdk.system.factory.SdkDaoFactory;

public class DBOrgTypeInfoFacade extends BaseFacade implements OrgTypeInfoFacade{

	@Override
	public List<OrgTypeInfo> find(Map<String, String> param, int currentPage,
			int pageSize) {
		return SdkDaoFactory.createOrgTypeInfoDao().find(param, currentPage, pageSize);
	}

	@Override
	public int findTotal(Map<String, String> param) {
		return SdkDaoFactory.createOrgTypeInfoDao().findTotal(param);
	}

	@Override
	public List<OrgTypeInfo> findChildsByParentId(String parentId) {
		return SdkDaoFactory.createOrgTypeInfoDao().findChildsByParentId(parentId);
	}

	@Override
	public OrgTypeInfo findByID(String id) {
		return SdkDaoFactory.createOrgTypeInfoDao().findByID(id);
	}

	@Override
	public OrgTypeInfo save(OrgTypeInfo orgTypeInfo) {
		return SdkDaoFactory.createOrgTypeInfoDao().save(orgTypeInfo);
	}

	@Override
	public OrgTypeInfo update(OrgTypeInfo orgTypeInfo) {
		return SdkDaoFactory.createOrgTypeInfoDao().update(orgTypeInfo);
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		return SdkDaoFactory.createOrgTypeInfoDao().findMaxOrderNo(parentId);
	}

	@Override
	public void delete(String id) {
		SdkDaoFactory.createOrgTypeInfoDao().delete(id);
	}

	@Override
	public void delete(OrgTypeInfo orgTypeInfo) {
		if(StringUtils.isNotBlank(orgTypeInfo.getId())){
			this.delete(orgTypeInfo.getId());
		}
	}

	@Override
	public void delete(List<OrgTypeInfo> orgTypeInfos) {
		StringBuilder ids = new StringBuilder();
		for (OrgTypeInfo orgTypeInfo : orgTypeInfos) {
			ids.append(orgTypeInfo.getId()).append(",");
		}
		ids.substring(0, ids.length()-1);
		this.delete(ids.toString());
	}

	@Override
	public List<OrgTypeInfo> findByCondition(Map<String, String> param) {
		return SdkDaoFactory.createOrgTypeInfoDao().findByCondition(param);
	}

	@Override
	public List<OrgTypeInfo> findAll() {
		return SdkDaoFactory.createOrgTypeInfoDao().findAll();
	}
	
	
}
