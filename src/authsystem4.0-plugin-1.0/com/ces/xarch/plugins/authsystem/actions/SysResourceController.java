package com.ces.xarch.plugins.authsystem.actions;

import ces.sdk.util.StringUtil;
import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.plugins.authsystem.service.SysSystemService;
import org.springframework.beans.factory.annotation.Autowired;

import com.ces.xarch.core.security.entity.SysResource;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceController;
import com.ces.xarch.plugins.authsystem.service.SysResourceService;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;


public class SysResourceController extends StringIDDefineServiceAuthSystemController<SysResource, SysResourceService>{

	
	@Override
	protected void initModel() {
		super.setModel(new SysResource());
	}

	
	@Autowired
	@Override
	protected void setService(SysResourceService service) {
		super.setService(service);
	}

    /**
     * 获取左侧同步树
     * @return
     */
    public Object getSyncTree(){
		Map<String, String> param = new HashMap<String, String>();
		String name = getParameter("name");
		if (StringUtil.isNotBlank(name)) {
			param.put("name", name);
		}
        Map<String,Object> resultMap = new HashMap<String, Object>();
        List<Map<String,Object>> systemMaps = getService().getSyncTree(param);
        resultMap.put("id", -1);
        resultMap.put("name", "资源管理");
        resultMap.put("open", true);
        resultMap.put("children", systemMaps);
		setReturnData(resultMap);
		return SUCCESS;
    }

	/**
	 * 获取系统树
	 * @return
	 */
	public Object getSystemTree(){
		Map<String, String> param = new HashMap<String, String>();
		String name = getParameter("name");
		if (StringUtil.isNotBlank(name)) {
			param.put("name", name);
		}
		List<SysSystem> sysSystems = getService(SysSystemService.class).getAllSystems(param);
		for (SysSystem sysSystem : sysSystems) {
			sysSystem.setIsParent(getService(SysSystemService.class).hasResource(sysSystem.getId()));
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();//result
		resultMap.put("id", -1);
		resultMap.put("name", "资源管理");
		resultMap.put("open", true);
		resultMap.put("children", sysSystems);
		setReturnData(resultMap);
		return SUCCESS;
	}

	/**
	 * 加载资源列表
	 * @return
	 */
	public Object listResource() {
		String treeNodeId = getParameter("treeNodeId");//点击节点id
		String isTree = getParameter("isTree");//是否树操作
		String isSystem = getParameter("isSystem");//点击系统节点
		boolean isSystemClick = false;
		if (isSystem != null && isSystem.equals("true")) {
			isSystemClick = true;
		}
		Specification<SysResource> spec = this.buildSpecification();
		if (isTree != null && isTree.equals("true")) {//树展开操作
			List<SysResource> sysResources = getService().listResource(spec, treeNodeId, isSystemClick);
			String expIds = getParameter("expIds");//移动资源时需要排除的树节点
			String selId = getParameter("selId");//资源树选中节点ID,设为无法选中
			if (StringUtil.isNotBlank(expIds)) {
				List<String> expIdsList = Arrays.asList(expIds.split(","));
				Iterator<SysResource> iterator = sysResources.iterator();
				while (iterator.hasNext()) {
					SysResource sysResource = iterator.next();
					if (sysResource.getId().equals(selId)) {
						sysResource.setNocheck(true);
					}
					if (expIdsList.contains(sysResource.getId())) {
						iterator.remove();
					}
				}
			}
			setReturnData(sysResources);
		} else {//列表操作
			PageRequest pageRequest = this.buildPageRequest();
			list = this.getDataModel(this.getModelTemplate());
			if (pageRequest == null) {
				list.setData(getService().listResource(spec, treeNodeId, isSystemClick));
			} else {
				list.setData(getService().listResource(spec, pageRequest, treeNodeId, isSystemClick));
			}
			setReturnData(list);
		}
		return SUCCESS;
	}

	/**
	 * 添加资源
	 * @return
	 */
	public Object addResource() {
		String treeNodeId = getParameter("treeNodeId");
		String isSystem = getParameter("isSystem");
		setReturnData(getService().addResource(model, treeNodeId, (isSystem != null && isSystem.equals("true"))));
		return SUCCESS;
	}

	/**
	 * 根据选中树节点获取所属系统
	 * @return
	 */
	public Object getSystemByTreeNode() {
		String treeNodeId = getParameter("treeNodeId");
		String isSystem = getParameter("isSystem");
		setReturnData(getService().getSystemByTreeNode(treeNodeId,(isSystem != null && isSystem.equals("true"))));
		return SUCCESS;
	}

	/**
	 * 移动资源
	 * @return
	 */
	public Object moveResource() {
		String resId = getParameter("resId");
		String newPid = getParameter("newPid");
		getService().moveResource(newPid, resId.split(","));
		setReturnData("OK");
		return SUCCESS;
	}

}
