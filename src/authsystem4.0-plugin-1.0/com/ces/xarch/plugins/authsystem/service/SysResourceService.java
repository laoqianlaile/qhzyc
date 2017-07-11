package com.ces.xarch.plugins.authsystem.service;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.facade.OrgTypeInfoFacade;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.util.StringUtil;

import com.ces.xarch.core.security.entity.SysResource;
import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.global.Constants;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wj copy from gj
 */
@Component
public class SysResourceService extends StringIDAuthSystemService<SysResource> {

	@Override
	public void bindingDao(Class<SysResource> entityClass) {
	}

	@Override
	public SysResource getByID(String id) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		ResourceInfo resourceInfo = resourceInfoFacade.findByID(id);
		return (SysResource) BeanConvertUtil.converter(resourceInfo,
				new SysResource());
	}

	public boolean hasChild(String parentId) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		List<ResourceInfo> resourceInfos = resourceInfoFacade
				.findChildsByParentId(parentId);
		if (resourceInfos.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<SysResource> findChildsByParentId(String parentId) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		List<SysResource> sysResources = new ArrayList<SysResource>();

		if (StringUtil.isBlank(parentId)) {
			SysResource sysResource = this.getByID(Constants.Resource.TOP);
			sysResource.setIsParent(this.hasChild(sysResource.getId()));
			sysResources.add(sysResource);
		} else {
			List<ResourceInfo> resourceInfos = resourceInfoFacade
					.findChildsByParentId(parentId);
			for (ResourceInfo resourceInfo : resourceInfos) {
				SysResource sysResource = (SysResource) BeanConvertUtil
						.converter(resourceInfo, new SysResource());
				sysResource.setIsParent(this.hasChild(sysResource.getId()));
				sysResources.add(sysResource);
			}
		}
		return sysResources;
	}

	@Override
	public void delete(String id) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		List<SysResource> allChildSysResources = new ArrayList<SysResource>();
		// 查询所有的子资源, 一并删除
		allChildSysResources = this.findAllChildsByParentId(id,
				allChildSysResources);
		StringBuilder ids = new StringBuilder();
		for (SysResource sysResource : allChildSysResources) {
			ids.append(sysResource.getId()).append(",");
		}
		ids.append(id);
		resourceInfoFacade.delete(ids.toString());

	}

	/**
	 * 查找所有的子组织级别
	 * 
	 * wj cp from gj
	 */
	public List<SysResource> findAllChildsByParentId(String parentId,
			List<SysResource> sysResourcesAll) {
		List<SysResource> childSysResources = this
				.findChildsByParentId(parentId);
		for (SysResource sysResource : childSysResources) {
			sysResourcesAll.add(sysResource);
			sysResourcesAll = findAllChildsByParentId(sysResource.getId(),
					sysResourcesAll);
		}
		return sysResourcesAll;
	}

	@Override
	@Transactional
	public SysResource update(SysResource sysResource) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();

		ResourceInfo resourceInfo = (ResourceInfo) BeanConvertUtil.converter(
				sysResource, new ResourceInfo());
		resourceInfoFacade.update(resourceInfo);
		return sysResource;
	}

	@Override
	public Page<SysResource> find(Specification<SysResource> spec,
			Pageable pageable) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		List<SysResource> sysResources = new ArrayList<SysResource>();
		Map<String, String> param = BeanConvertUtil.getFilter(spec);
		int total = resourceInfoFacade.findTotal(param);
		List<ResourceInfo> resourceInfos = resourceInfoFacade.find(param,
				pageable.getPageNumber() + 1, pageable.getPageSize());
		sysResources = this.converterResourceInfo(resourceInfos, sysResources);
		return new PageImpl<SysResource>(sysResources, pageable, total);
	}

	@Override
	protected long findMaxOrderNo(SysResource sysResource) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		long maxOrder = resourceInfoFacade.findMaxOrderNo(sysResource
				.getParentId());
		return maxOrder + 1;
	}

	@Override
	@Transactional
	public SysResource save(SysResource sysResource) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil
				.getResourceInfoFacade();
		sysResource.setShowOrder(this.findMaxOrderNo(sysResource));

		ResourceInfo resourceInfo = (ResourceInfo) BeanConvertUtil.converter(
				sysResource, new ResourceInfo());
		resourceInfo = resourceInfoFacade.save(resourceInfo);

		return (SysResource) BeanConvertUtil.converter(resourceInfo,
				new SysResource());
	}

	@Override
	public List<SysResource> find(Specification<SysResource> spec) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		List<SysResource> sysResources = new ArrayList<SysResource>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		List<ResourceInfo> resourceInfos = resourceInfoFacade.findByCondition(param);
		sysResources = this.converterResourceInfo(resourceInfos, sysResources);
		return sysResources;
	}

	private List<SysResource> converterResourceInfo(List<ResourceInfo> resourceInfos, List<SysResource> sysResources) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		for (ResourceInfo resourceInfo : resourceInfos) {
			SysResource sysResource = (SysResource) BeanConvertUtil.converter(resourceInfo, new SysResource());
			sysResource.setIsParent(this.hasChild(sysResource.getId()));
			sysResources.add(sysResource);
		}
		return sysResources;
	}

	private List<ResourceInfo> converterSysResource(
			List<SysResource> sysResources, List<ResourceInfo> resourceInfos) {
		for (SysResource sysResource : sysResources) {
			resourceInfos.add((ResourceInfo) BeanConvertUtil.converter(
					sysResource, new ResourceInfo()));
		}
		return resourceInfos;
	}

	/**
	 * 加载资源列表
	 * @param treeNodeId
	 * @return
	 */
	public Page<SysResource> listResource(Specification<SysResource> spec,Pageable pageable, String treeNodeId, boolean isSystem) {
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		List<SysResource> sysResources = new ArrayList<SysResource>();
		List<ResourceInfo> resourceInfos = null;
		int total = 0;
		if (isSystem) {//选中的是系统节点
			if (treeNodeId.length() == 32) {//建立的系统
				param.put("parentId", "-1");
			} else {//默认系统节点
				param.put("parentId", "1");
			}
			total = resourceInfoFacade.findResourcesTotalBySystemId(treeNodeId, param);
			resourceInfos = resourceInfoFacade.findResourcesPageBySystemId(treeNodeId, param, pageable.getPageNumber() + 1, pageable.getPageSize());
		} else {//选中的是资源节点
			param.put("parentId", treeNodeId);
			total = resourceInfoFacade.findTotal(param);
			resourceInfos = resourceInfoFacade.find(param, pageable.getPageNumber() + 1, pageable.getPageSize());
		}
		sysResources = this.converterResourceInfo(resourceInfos, sysResources);
		return new PageImpl<SysResource>(sysResources, pageable, total);
	}

	/**
	 * 加载资源列表
	 * @param treeNodeId
	 * @return
	 */
	public List<SysResource> listResource(Specification<SysResource> spec,String treeNodeId, boolean isSystem) {
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		List<SysResource> sysResources = new ArrayList<SysResource>();
		List<ResourceInfo> resourceInfos = null;
		if (isSystem) {//选中的是系统节点
			if (treeNodeId.length() == 32) {//建立的系统
				param.put("parentId", "-1");
			} else {//默认系统节点
				param.put("parentId", "1");
			}
			resourceInfos = resourceInfoFacade.findResourcesBySystemId(treeNodeId, param);
		} else {//选中的是资源节点
			param.put("parentId", treeNodeId);
			resourceInfos = resourceInfoFacade.findByCondition(param);
		}
		for (ResourceInfo resourceInfo : resourceInfos) {
			SysResource sysResource = (SysResource) BeanConvertUtil.converter(
					resourceInfo, new SysResource());
			sysResource.setIsParent(this.hasChild(sysResource.getId()));
			sysResources.add(sysResource);
		}
		return sysResources;
	}

	/**
	 * 添加资源
	 */
	public SysResource addResource(SysResource sysResource, String treeNodeId, boolean isSystem) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		String systemId;
		if (isSystem) {//选中的是系统节点
			sysResource.setParentId("-1");
			systemId = treeNodeId;
		} else {//选中的是资源节点
			systemId = resourceInfoFacade.findSystemIdByResource(treeNodeId);
		}
		sysResource.setShowOrder(this.findMaxOrderNo(sysResource));
		ResourceInfo resourceInfo = (ResourceInfo) BeanConvertUtil.converter(sysResource, new ResourceInfo());
		resourceInfo = resourceInfoFacade.save(resourceInfo, systemId);
		sysResource = (SysResource) BeanConvertUtil.converter(resourceInfo, new SysResource());
		sysResource.setIsParent(false);
		return sysResource;
	}

	/**
	 * 获取单个系统资源静态树
	 * @param treeNodeId 选中的树节点ID
	 */
	public SysSystem getSystemByTreeNode(String treeNodeId, boolean isSystem) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		String systemId;
		if (isSystem) {//选中的是系统节点
			systemId = treeNodeId;
		} else {//选中的是资源节点
			systemId = resourceInfoFacade.findSystemIdByResource(treeNodeId);
		}
		SystemInfo systemInfo = systemFacade.findByID(systemId);
		SysSystem sysSystem = (SysSystem)BeanConvertUtil.converter(systemInfo, new SysSystem());
		sysSystem.setIsParent(true);
		sysSystem.setNocheck(true);
		return sysSystem;
	}

	/**
	 * 移动资源
	 * @param newPid 新父ID
	 * @param resIds 资源ID
	 */
	public void moveResource(String newPid, String[] resIds) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		resourceInfoFacade.moveResource(newPid, resIds);
	}

	/**
	 * 获取左侧同步树
	 * @return
	 */
	public List<Map<String, Object>> getSyncTree(Map<String,String> param) {
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		List<Map<String,Object>> systemMaps = new ArrayList<Map<String, Object>>();
		List<SysSystem> sysSystems = getService(SysSystemService.class).getAllSystems(param);
		for (SysSystem sysSystem : sysSystems) {
			Map<String,Object> systemMap = new HashMap<String, Object>();
			boolean hasResource = getService(SysSystemService.class).hasResource(sysSystem.getId());
			//获取第一层资源
			List<SysResource> sysResources = new ArrayList<SysResource>();
			Map<String,String> resourceParam = new HashMap<String, String>();
			if ("1".equals(sysSystem.getId())) {
				resourceParam.put("parentId","1");
			} else {
				resourceParam.put("parentId","-1");
			}
			List<ResourceInfo> firstLevelResources = resourceInfoFacade.findResourcesBySystemId(sysSystem.getId(),resourceParam);
			sysResources = converterResourceInfo(firstLevelResources, sysResources);
			//获取所有资源
			List<Map<String,Object>> resourceMaps = getResrouceTree(sysResources);

			systemMap.put("id",sysSystem.getId());
			systemMap.put("treeNodeType","system");
			systemMap.put("name", sysSystem.getName());
			systemMap.put("isParent",hasResource);
			systemMap.put("children",resourceMaps);
			systemMaps.add(systemMap);
		}
		return systemMaps;
	}

	/**
	 * 获取同步资源树
	 * @param sysResources
	 * @return
	 */
	public List<Map<String, Object>> getResrouceTree(List<SysResource> sysResources){
		ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
		List<Map<String,Object>> resultMaps = new ArrayList<Map<String, Object>>();
		for (SysResource sysResource : sysResources) {
			Map<String,Object> resultMap = new HashMap<String, Object>();
			resultMap.put("id",sysResource.getId());
			resultMap.put("name",sysResource.getName());
			resultMap.put("treeNodeType","resource");
			resultMap.put("isParent",sysResource.getIsParent());

			if(sysResource.getIsParent()){
				List<SysResource> childSysResources = new ArrayList<SysResource>();
				//查找子节点
				Map<String,String> param = new HashMap<String, String>();
				param.put("parentId",sysResource.getId());
				List<ResourceInfo> resourceInfos = resourceInfoFacade.findByCondition(param);
				childSysResources = converterResourceInfo(resourceInfos, childSysResources);
				//put children
				List<Map<String,Object>> childMaps = getResrouceTree(childSysResources);
				resultMap.put("children",childMaps);

			} else {
			}
			resultMaps.add(resultMap);
		}
		return resultMaps;
	}
}