package com.ces.xarch.plugins.authsystem.actions;

import ces.sdk.util.JsonUtil;
import ces.sdk.util.StringUtil;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;
import com.ces.xarch.core.security.entity.SysOrgType;
import com.ces.xarch.core.security.entity.SysRole;
import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.plugins.authsystem.service.SysRoleService;
import com.ces.xarch.plugins.authsystem.service.SysSystemService;
import com.ces.xarch.plugins.common.global.Constants;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysRoleController extends StringIDDefineServiceAuthSystemController<SysRole, SysRoleService> {
	
	@Override
	protected void initModel() {
		super.setModel(new SysRole());
	}

	
	@Autowired
	@Override
	protected void setService(SysRoleService service) {
		super.setService(service);
	}

    /**
     * 获取同步树
     * @return
     */
    public Object getSyncTree () {
        Map<String,String> param = new HashMap<String, String>();
        String name = getParameter("name");
        if (StringUtil.isNotBlank(name)) {
            param.put("name", name);
        }
        List<Map<String,Object>> sysSystemMaps = getService().getSyncTree(param);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", "-1");
        resultMap.put("name", "角色管理");
        resultMap.put("open", true);
        resultMap.put("children", sysSystemMaps);
        setReturnData(resultMap);
        return SUCCESS;
    }

	//加载默认系统树
	public Object getSystemTree() {
        Map<String,String> param = new HashMap<String, String>();
        String name = getParameter("name");
        if (StringUtil.isNotBlank(name)) {
            param.put("name", name);
        }
		List<SysSystem> sysSystems = getService(SysSystemService.class).getAllSystems(param);
        for(SysSystem sysSystem:sysSystems){
            sysSystem.setIsParent(getService(SysSystemService.class).hasRole(sysSystem.getId()));
        }
		Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", "-1");
        resultMap.put("name", "角色管理");
        resultMap.put("open", true);
        resultMap.put("children", sysSystems);
		setReturnData(resultMap);
		return SUCCESS;
	}

	/***
	 * 根据系统ID获取角色
	 * @return
	 */
	public Object getRoleList() {
        String treeNodeId = getParameter("treeNodeId");
        String isTree = getParameter("isTree");
        Specification<SysRole> spec = this.buildSpecification();
        if (isTree != null && isTree.equals("true")) {//树展开操作
            setReturnData(getService().getRoleList(spec, treeNodeId));
        } else {//列表操作
            PageRequest pageRequest = this.buildPageRequest();
            list = this.getDataModel(this.getModelTemplate());
            if (pageRequest == null) {
                list.setData(getService().getRoleList(spec, treeNodeId));
            } else {
                list.setData(getService().getRoleList(spec, pageRequest, treeNodeId));
            }
            setReturnData(list);
        }
        return SUCCESS;
	}

    /**
     * 获取角色下的资源列表
     * @return
     */
    public Object getResourceByRole(){
        String roleId = getParameter("treeNodeId");
        PageRequest pageRequest = this.buildPageRequest();
	    Specification<SysRole> spec = this.buildSpecification();
        list = this.getDataModel(this.getModelTemplate());
        list.setData(getService().getResourceByRole(spec, pageRequest,roleId));
        setReturnData(list);
        return SUCCESS;
    }

    /**
     *保存role以及sysRole
     *@return
     *
     */
    @Override
    @Logger(action="添加",logger="${id}")
    public Object create() throws FatalException {
        String systemId = getParameter("systemId");
        setReturnData(this.getService().save(model, systemId));
        return SUCCESS;
    }

    /**
     * 根据树节点获取系统对应
     * @return
     */
    public Object getResourceTreeByTreeNode(){
        String treeNodeId = getParameter("treeNodeId");
        setReturnData(getService().getResourceTreeByTreeNode(treeNodeId));
        return SUCCESS;
    }

    /**
     * 为角色授予资源
     * @return
     */
    public Object grantResource(){
        String roleId = getParameter("roleId");
        String resIds = getParameter("resIds");
        getService().grantResource(roleId, resIds);
        return SUCCESS;
    }

    /**
     * 根据角色ID获取所有的用户
     * @return
     */
    public Object getUserInfosByRoleId() {
	    String roleId = getParameter("roleId");
	    Specification<SysRole> spec = this.buildSpecification();
	    PageRequest pageRequest = this.buildPageRequest();
	    list = this.getDataModel(this.getModelTemplate());
	    list.setData(getService().getUserInfosByRoleId(spec, pageRequest, roleId));
	    setReturnData(list);
	    return SUCCESS;
    }

	/**
	 * 授权用户角色
	 * @return
	 */
	public Object authorizeUser() {
        List<Map<String, String>> userAndOrgIds = JsonUtil.fromJSON(getParameter("userAndOrgIds"), new TypeReference<List<Map<String, String>>>() {});
		String dateStart = null;
		String dateEnd = null;
        String isTempAccredit = getParameter("isTempAccredit");
		if ("1".equals(isTempAccredit)) {//启用临时授权
            dateStart = getParameter("dateStart");
            dateEnd = getParameter("dateEnd");
		}
		getService().authorizeUser(model.getId(), userAndOrgIds, isTempAccredit, dateStart, dateEnd);
		return SUCCESS;
	}

	/**
	 * 移除用户角色
	 * @return
	 */
	public Object unAuthorizeUser() {
		List<Map<String,String>> userAndOrgIds = JsonUtil.fromJSON(getParameter("userAndOrgIds"), new TypeReference<List<Map<String,String>>>() {});
		getService().unAuthorizeUser(model.getId(), userAndOrgIds);
		return SUCCESS;
	}

	
    /**
     * 删除角色下的资源
     */
    public Object removeResource() {
        String ids = getParameter("id");
        String roleId = getParameter("roleId");
        getService().removeResource(roleId, ids);
        return SUCCESS;
    }

    /**
     *  获取系统管理平台自定义角色(屏蔽内置角色)
     */
    public Object getRolesByAuthSystemID(){
    	setReturnData(getService().findRoleBySystemIdAndOrgId(Constants.System.AUTHSYSTEM, Constants.Org.TOP));
    	return SUCCESS;
    }
    
	/**
	 * 获取所有的组织级别
	 * @return
	 */
    public Object getAllOrgTypeList() {
	    List<SysOrgType> allOrgTypeList = getService().getAllOrgTypeList();
	    setReturnData(allOrgTypeList);
	    return SUCCESS;
    }

    public Object checkRoleNameUnique() {
        String rolename = getParameter("rolename");
        String systemId = getParameter("systemId");
        setReturnData(getService().checkRoleNameUnique(rolename, systemId));
        return SUCCESS;
    }
}
