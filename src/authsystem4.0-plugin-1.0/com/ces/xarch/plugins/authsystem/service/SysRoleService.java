package com.ces.xarch.plugins.authsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.RoleUserInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.facade.UserInfoFacade;

import com.ces.xarch.core.security.entity.SysOrgType;
import com.ces.xarch.core.security.entity.SysResource;
import com.ces.xarch.core.security.entity.SysRole;
import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.security.vo.SystemRoleVO;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.global.Constants;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

@Component
public class SysRoleService extends StringIDAuthSystemService<SysRole>{

	@Override
	public void bindingDao(Class<SysRole> entityClass) {
	}

	/* (non-Javadoc)
			 * @see com.ces.xarch.core.service.AbstractService#find(org.springframework.data.jpa.domain.Specification)
			 */
	@Override
	public List<SysRole> find(Specification<SysRole> spec) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        List<SysRole> sysRoles = new ArrayList<SysRole>();
        Map<String,String> param = BeanConvertUtil.getFilter(spec);
        List<RoleInfo> roleInfos = roleInfoFacade.findByCondition(param);
        sysRoles = this.converterRoleInfo(roleInfos, sysRoles);
        return sysRoles;
	}


	@Override
	public Page<SysRole> find(Specification<SysRole> spec, Pageable pageable) {
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		Map<String, String> paramMap = BeanConvertUtil.getFilter(spec);
		
		List<RoleInfo> roleInfos = roleInfoFacade.find(paramMap, pageable.getPageNumber() + 1, pageable.getPageSize());
		
		
		return null;
	}


	@Override
	public List<SysRole> find(Specification<SysRole> spec, Sort sort) {
		return super.find(spec, sort);
	}

	@Override
	public SysRole save(SysRole sysRole) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 新增保存
     * @param sysRole
     * @param systemId
     * @return
     */
    public SysRole save(SysRole sysRole,String systemId) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        sysRole.setShowOrder(this.findMaxOrderNo(systemId));

        RoleInfo roleInfo = (RoleInfo) BeanConvertUtil.converter(sysRole, new RoleInfo());
        roleInfo = roleInfoFacade.save(roleInfo,systemId);
        return (SysRole) BeanConvertUtil.converter(roleInfo, new SysRole());
    }

    /**
     * 修改角色
     * @param sysRole
     * @return
     */
	@Override
	public SysRole update(SysRole sysRole) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();

        RoleInfo roleInfo = (RoleInfo) BeanConvertUtil.converter(sysRole, new RoleInfo());
        roleInfoFacade.update(roleInfo);
        return sysRole;
	}

    /**
     * 获取系统下角色最大showOrder
     * @param systemId
     * @return
     */
	@Override
	protected long findMaxOrderNo(SysRole sysRole) {
        return 0;
	}


    private long findMaxOrderNo(String systemId) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        long maxOrder = roleInfoFacade.findMaxOrderNo(systemId);
        return maxOrder + 1;
    }

    /**
     * 删除||批量删除角色
     * @param id
     */
	@Override
	public void delete(String id) {
	    RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        roleInfoFacade.delete(id);
    }

    /**
     * 根据id获取角色
     * @param id
     * @return
     */
	@Override
	public SysRole getByID(String id) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        RoleInfo roleInfo = roleInfoFacade.findByID(id);
        return (SysRole) BeanConvertUtil.converter(roleInfo, new SysRole());
	}

    /**
     * 树获取角色||列表无分页获取角色
     * @param spec
     * @param treeNodeId
     * @return
     */
    public Object getRoleList(Specification<SysRole> spec, String treeNodeId) {
        Map<String,String> param = BeanConvertUtil.getFilter(spec);
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        List<SysRole> sysRoles = new ArrayList<SysRole>();
        List<RoleInfo> roleInfos = roleInfoFacade.findRolesBySystemId(treeNodeId, param);
        for (RoleInfo roleInfo : roleInfos) {
            SysRole sysRole = (SysRole) BeanConvertUtil.converter(
                    roleInfo, new SysRole());
	        if (currentUserhasRole(sysRole.getRoleKey())) {//有权限的才在树上显示
		        sysRoles.add(sysRole);
	        }
        }
        return sysRoles;
    }
    
    /**
     * 根据系统ID和组织id查询角色, 如果组织是根组织, 则返回该系统下的所有角色
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月2日 下午2:00:41
     * @param systemId
     * @param parentOrgId
     * @return
     */
    public List<SysRole> findRoleBySystemIdAndOrgId(String systemId, String parentOrgId){
    	RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
    	
    	List<RoleInfo> roleInfos = null;
    	if(Constants.Org.TOP.equals(parentOrgId)){
    		roleInfos = roleInfoFacade.findRolesBySystemId(systemId, null);
    	}else {
    		roleInfos = roleInfoFacade.findRolesBysystemIdAndOrgId(systemId, parentOrgId);
    	}
    	
    	
        List<SysRole> sysRoles = new ArrayList<SysRole>();
        //转换角色的同时, 将角色的父id设置为系统id
        for (RoleInfo roleInfo : roleInfos) {
        	SysRole sysRole = (SysRole) BeanConvertUtil.converter(roleInfo, new SysRole());
        	sysRole.setParentId(systemId);
        	sysRoles.add(sysRole);
        }
        roleInfos = null;
        return sysRoles;
    }
    
    /**
     * 列表分页获取角色
     * @param spec
     * @param pageable
     * @param treeNodeId
     * @return
     */
    public Object getRoleList(Specification<SysRole> spec, Pageable pageable, String treeNodeId) {
        Map<String,String> param = BeanConvertUtil.getFilter(spec);
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        List<SysRole> sysRoles = new ArrayList<SysRole>();
        List<RoleInfo> roleInfos = null;
        int total = 0;
        total = roleInfoFacade.findTotal(treeNodeId, param);
        roleInfos = roleInfoFacade.findRolePageBySystemId(treeNodeId, param, pageable.getPageNumber() + 1, pageable.getPageSize());
        sysRoles = this.converterRoleInfo(roleInfos, sysRoles);
        return new PageImpl<SysRole>(sysRoles, pageable, total);
    }

    private List<SysRole> converterRoleInfo(List<RoleInfo> roleInfos, List<SysRole> sysRoles) {
        for (RoleInfo roleInfo : roleInfos) {
            sysRoles.add((SysRole) BeanConvertUtil.converter(
                    roleInfo, new SysRole()));
        }
        return sysRoles;
    }

    private List<SysResource> converterResourceInfo(List<ResourceInfo> resourceInfos, List<SysResource> sysResources) {
        for (ResourceInfo resourceInfo : resourceInfos) {
            sysResources.add((SysResource) BeanConvertUtil.converter(
                    resourceInfo, new SysResource()));
        }
        return sysResources;
    }

    /**
     * 设置展开及是否选中
     *
     */
    private List<SysResource> converterResourceInfoSetOpen(List<ResourceInfo> resourceInfos, List<SysResource> sysResources,String roleId) {
        ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        for (ResourceInfo resourceInfo : resourceInfos) {
            SysResource sysResource = null;
            boolean hasChild = getService(SysResourceService.class).hasChild(resourceInfo.getId());
            sysResource = (SysResource) BeanConvertUtil.converter(resourceInfo, new SysResource());
            sysResource.setIsParent(hasChild);
            sysResource.setOpen(hasChild);
            sysResource.setChecked(roleInfoFacade.hasResource(roleId,resourceInfo.getId()));
            sysResources.add(sysResource);
        }
        return sysResources;
    }

    /**
     * 根据树节点获取对应系统及资源树
     * @param treeNodeId
     * @return
     */
    public Map<String,Object> getResourceTreeByTreeNode(String treeNodeId) {
        SystemFacade systemFacade = FacadeUtil.getSystemFacade();
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
        Map<String,Object> resultMap = new HashMap<String, Object>();//resultMap

        //获取第一级资源
        String systemId = roleInfoFacade.findSystemIdByRole(treeNodeId);//根据角色id获取系统
        List<SysResource> sysResources = new ArrayList<SysResource>();
        Map<String,String> param = new HashMap<String, String>();
        param.put("parentId","-1");
        List<ResourceInfo> firstLevelResources = resourceInfoFacade.findResourcesBySystemId(systemId,param);
        sysResources = converterResourceInfoSetOpen(firstLevelResources, sysResources,treeNodeId);
        List<Map<String,Object>> resourceMaps = findResource(sysResources,treeNodeId);

        SystemInfo systemInfo = systemFacade.findByID(systemId);
        SysSystem sysSystem = (SysSystem)BeanConvertUtil.converter(systemInfo,new SysSystem());
        resultMap.put("id",-1);
        resultMap.put("name",sysSystem.getName());
        resultMap.put("open",true);
        resultMap.put("nocheck",true);
        resultMap.put("children",resourceMaps);
        return resultMap;
    }

    /**
     * 角色授予资源生成资源树map:
     * @param sysResources
     * @return
     */
    public List<Map<String,Object>> findResource(List<SysResource> sysResources,String roleId){
        ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
        List<Map<String,Object>> resultMaps = new ArrayList<Map<String, Object>>();
        for (SysResource sysResource : sysResources) {
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("id",sysResource.getId());
            resultMap.put("name",sysResource.getName());
            resultMap.put("open",sysResource.getOpen());
            resultMap.put("checked",sysResource.getChecked());

            if(sysResource.getIsParent()){
                List<SysResource> childSysResources = new ArrayList<SysResource>();
                //查找子节点
                Map<String,String> param = new HashMap<String, String>();
                param.put("parentId",sysResource.getId());
                List<ResourceInfo> resourceInfos = resourceInfoFacade.findByCondition(param);
                childSysResources = converterResourceInfoSetOpen(resourceInfos,childSysResources,roleId);
                //put children
                List<Map<String,Object>> childMaps = findResource(childSysResources,roleId);
                resultMap.put("children",childMaps);

            } else {
            }
            resultMaps.add(resultMap);
        }
        return resultMaps;
    }

    /**
     * 获取角色拥有的资源
     *
     * @param pageable
     * @param roleId
     * @return
     */
    public Object getResourceByRole(Specification<SysRole> spec,Pageable pageable, String roleId) {
	    Map<String,String> param = BeanConvertUtil.getFilter(spec);
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
        List<SysResource> sysResources = new ArrayList<SysResource>();
        List<ResourceInfo> resourceInfos = null;
        int total = 0;
        total = roleInfoFacade.findResourceTotal(roleId,param);
        resourceInfos = resourceInfoFacade.findResourcesPageByRoleId(roleId,param, pageable.getPageNumber() + 1, pageable.getPageSize());
        sysResources =  this.converterResourceInfo(resourceInfos, sysResources);
        return new PageImpl<SysResource>(sysResources, pageable, total);
    }

    public void grantResource(String roleId, String resIds) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        roleInfoFacade.grantResource(roleId,resIds);
    }

    private List<SysUser> converterUserInfo(List<UserInfo> userInfos, List<SysUser> users) {
        for (UserInfo userInfo : userInfos) {
            users.add((SysUser) BeanConvertUtil.converter(
                    userInfo, new SysUser()));
        }
        return users;
    }

    /**
     * 根据角色ID获取所有的用户
     * @param roleId
     * @return
     */
	public Page<Map<String, String>> getUserInfosByRoleId(Specification<SysRole> spec, Pageable pageable, String roleId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
        param.put("roleId", roleId);
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		UserInfoFacade userInfoFacade = FacadeUtil.getUserInfoFacade();
		OrgInfoFacade orgInfoFacade = FacadeUtil.getOrgInfoFacade();
		int total = roleInfoFacade.findUserInfosByRoleIdTotal(param);
		List<RoleUserInfo> roleUserInfos = roleInfoFacade.findUserInfosByRoleIdPage(param, pageable.getPageNumber() + 1, pageable.getPageSize());
		for (RoleUserInfo roleUserInfo : roleUserInfos) {
			Map<String, String> map = new HashMap<String, String>();
			UserInfo userInfo = userInfoFacade.findByID(roleUserInfo.getUserId());
			OrgInfo orgInfo = orgInfoFacade.findByID(roleUserInfo.getOrgId());
			map.put("id", roleUserInfo.getId());
			map.put("userId", roleUserInfo.getUserId());
			map.put("userLoginName", userInfo.getLoginName());
			map.put("userName", userInfo.getName());
			map.put("orgId", roleUserInfo.getOrgId());
			map.put("orgName", orgInfo.getName());
			list.add(map);
		}
		return new PageImpl<Map<String, String>>(list, pageable, total);
	}

	/**
	 * 授权用户角色
	 * @param roleId
	 * @param userAndOrgIds
	 */
	public void authorizeUser(String roleId, List<Map<String, String>> userAndOrgIds,String isTempAccredit, String dateStart, String dateEnd) {
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		roleInfoFacade.authorizeUser(roleId, userAndOrgIds, isTempAccredit, dateStart, dateEnd);
	}

    /**
     * 移除用户授权
     * @param roleId
     * @param userAndOrgIds
     */
    public void unAuthorizeUser(String roleId, List<Map<String,String>> userAndOrgIds) {
	    RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
	    roleInfoFacade.unAuthorizeUser(roleId, userAndOrgIds);
    }

    public void removeResource(String roleId, String ids) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        ResourceInfoFacade resourceInfoFacade = FacadeUtil.getResourceInfoFacade();
        roleInfoFacade.removeResource(roleId,ids);
        String[] idArray = ids.split(",");
        for (int i = 0;i<idArray.length;i++) {
        	boolean hasChild = getService(SysResourceService.class).hasChild(idArray[i]);
            if (hasChild) {
                Map<String,String> param = new HashMap<String,String>();
                param.put("parentId",idArray[i]);
                List<ResourceInfo> resourceInfos = resourceInfoFacade.findByCondition(param);
                StringBuilder childIds = new StringBuilder();
                for(ResourceInfo resourceInfo : resourceInfos){
                    childIds.append(resourceInfo.getId());
                    childIds.append(",");
                }
                childIds.deleteCharAt(childIds.length()-1);
                removeResource(roleId,childIds.toString());
            }
        }
    }

    /**
     * 获取一个组织级别的同步树
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月2日 下午2:07:26
     * @return
     */
    public List<SysOrgType> getAllOrgTypeList() {
	    List<SysOrgType>  allSysOrgTypes = getService(SysOrgTypeService.class).findAll();
	    Map<String,SysOrgType> entitysMap = new HashMap<String,SysOrgType>();
	    for (SysOrgType sysOrgType : allSysOrgTypes) {
		    entitysMap.put(sysOrgType.getId(), sysOrgType);
	    }
	    List<SysOrgType> result = new ArrayList<SysOrgType>();
	    // 组装List(不带层级)
	    for (Map.Entry<String, SysOrgType> entry : entitysMap.entrySet()) {
		    SysOrgType sysOrgType = entry.getValue();
		    if (entitysMap.containsKey(sysOrgType.getParentId())) {
			    result.add(sysOrgType);
		    }
	    }
	    return result;
    }

    /**
     * 获取同步树
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSyncTree(Map<String, String> param) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        List<SysSystem> sysSystems = getService(SysSystemService.class).getAllSystems(param);
        List<Map<String,Object>> sysSystemMaps = new ArrayList<Map<String, Object>>();
        for(SysSystem sysSystem:sysSystems){
            Map<String,Object> sysSystemMap = new HashMap<String, Object>();
            List<RoleInfo> roleInfos = roleInfoFacade.findRolesBySystemId(sysSystem.getId(), param);
            List<SysRole> sysRoles = new ArrayList<SysRole>();
            sysRoles = converterRoleInfo(roleInfos,sysRoles);
	        if (sysSystem.getId().length() < 32 && !isSuperRole()) {//系统管理平台节点,不是超级管理员
		        Iterator<SysRole> iterator = sysRoles.iterator();
		        while (iterator.hasNext()) {
			        SysRole sysRole = iterator.next();
			        if (currentUserhasRole(sysRole.getRoleKey())) {//有该角色的树上不显示
				        iterator.remove();
			        }
		        }
	        }
            sysSystemMap.put("id",sysSystem.getId());
            sysSystemMap.put("name",sysSystem.getName());
            sysSystemMap.put("treeNodeType",sysSystem.getTreeNodeType());
            sysSystemMap.put("isParent", getService(SysSystemService.class).hasRole(sysSystem.getId()));
            sysSystemMap.put("children",sysRoles);
            sysSystemMaps.add(sysSystemMap);
        }
        return sysSystemMaps;
    }

    /**
     * 查询该组织拥有的系统, 如果组织是根组织, 则返回全部的系统
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月2日 下午2:06:11
     * @param parentOrgId 组织id
     * @return
     */
    private List<SysSystem> getSystems4Grant(String parentOrgId){
    	List<SysSystem> result = new ArrayList<SysSystem>();
     	if(Constants.Org.TOP.equals(parentOrgId)){
     		result = this.getService(SysSystemService.class).findAll();
    	} else {
    		result = this.getService(SysSystemService.class).findSystemsByOrgId(parentOrgId);
    	}
    	return result;
    }
    
    /**
     * 用户授予角色, 生成一棵同步树
     * 
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月2日 下午1:48:50
     * @param userId
     * @param orgId
     * @return
     */
    public List<SystemRoleVO> getStaticSysRoleTree4UserGrantRole(String userId,String orgId){
    	List<SysSystem> sysSystems = this.getSystems4Grant(orgId);
    	List<SystemRoleVO> systemRoleVOs = new ArrayList<SystemRoleVO>();
    	
    	//获取要勾选的系统和角色id
		Map<String,Set<String>> checkedSysRole = this.findCheckedSystemAndRoleByUserId(userId);
    	
    	for (int i = sysSystems.size()-1; i >= 0; i--) {
			SysSystem sysSystem = sysSystems.get(i);
			//获取全部要勾选的系统ids
			Set<String> checkedSystemIds = checkedSysRole.get("systemIds");
			SystemRoleVO systemRoleVO = BeanConvertUtil.convert(sysSystem, null);
			if(checkedSysRole !=null && checkedSystemIds.contains(systemRoleVO.getId())){
				systemRoleVO.setChecked(true);
			}
			//获取全部要勾选的角色ids
			Set<String> checkedRoleIds = checkedSysRole.get("roleIds");
			
			//根据系统和组织 查询该系统和组织下, 拥有的角色
			List<SysRole> sysRoles = this.findRoleBySystemIdAndOrgId(sysSystem.getId(),orgId);
			if(checkedSysRole !=null){
				for (SysRole sysRole : sysRoles) {
					if(checkedRoleIds.contains(sysRole.getId())){
						sysRole.setChecked(true);
					}
				}
			}
			
			//给系统添加一级角色
			if(sysRoles.size() > 0){
				systemRoleVO.getChildren().addAll(sysRoles);
			} else {
				systemRoleVO.setIsParent(false); //如果系统下没有角色, 则不是父级节点
			}
			systemRoleVOs.add(systemRoleVO);
		}
    	
    	return systemRoleVOs;
    }
    
    /**
     * 组织授予角色, 获取一棵系统角色树 (静态的)
     * 
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月2日 下午1:49:10
     * @param orgId 选中的组织id (用于勾选该组织所拥有的角色)
     * @param parentOrgId 父组织id, 用于查询该组织拥有的系统角色
     * @return
     */
	public List<SystemRoleVO> getStaticSysRoleTree4OrgGrantRole(String orgId, String parentOrgId) {
		List<SysSystem> sysSystems = this.getSystems4Grant(parentOrgId);
		List<SystemRoleVO> systemRoleVOs = new ArrayList<SystemRoleVO>();
		//获取要勾选的系统和角色id
		Map<String,Set<String>> checkedSysRole = this.findCheckedSystemAndRoleByOrgId(orgId);
		
		//遍历每个系统, 查询每个系统下的角色, 并将角色放到系统的children里
		for (int i = sysSystems.size()-1; i >= 0; i--) {
			SysSystem sysSystem = sysSystems.get(i);
			
			//获取全部要勾选的系统ids
			Set<String> checkedSystemIds = checkedSysRole.get("systemIds");
			
			//不显示系统管理平台
			if(Constants.System.AUTHSYSTEM.equals(sysSystem.getId())){
				sysSystems.remove(i);
			} else {
				SystemRoleVO systemRoleVO = BeanConvertUtil.convert(sysSystem, null);
				
				if(checkedSysRole !=null && checkedSystemIds.contains(systemRoleVO.getId())){
					systemRoleVO.setChecked(true);
				}
				
				//获取全部要勾选的角色ids
				Set<String> checkedRoleIds = checkedSysRole.get("roleIds");
				//查询该系统和组织下, 拥有的角色
				List<SysRole> sysRoles = this.findRoleBySystemIdAndOrgId(sysSystem.getId(),parentOrgId);
				if(checkedSysRole !=null){
					for (SysRole sysRole : sysRoles) {
						if(checkedRoleIds.contains(sysRole.getId())){
							sysRole.setChecked(true);
						}
					}
				}
				
				//给系统添加一级角色
				if(sysRoles.size() > 0){
					systemRoleVO.getChildren().addAll(sysRoles);
				} else {
					systemRoleVO.setIsParent(false); //如果系统下没有角色, 则不是父级节点
				}
				systemRoleVOs.add(systemRoleVO);
			}
		}
		
		return systemRoleVOs;
	}
	
	/**
	 * 根据组织id, 查询该组织拥有系统和拥有的角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午1:52:49
	 * @param orgId
	 * @return map结构为:
	 * { key : systemId, value : Set<String> systemIds,
	 * 	 key : roleId, value : Set<String> roleIds
	 * }
	 */
	private Map<String, Set<String>> findCheckedSystemAndRoleByOrgId(String orgId) {
		if(StringUtils.isBlank(orgId)){
			return null;
		}
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		Map<String, Set<String>> checkedSysRole = roleInfoFacade.findCheckedSystemAndRoleByOrgId(orgId);
		return checkedSysRole;
	}
	
	/**
	 * 根据用户id, 查询该用户授予的系统和关联的角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午1:52:49
	 * @param orgId
	 * @return map结构为:
	 * { key : systemId, value : Set<String> systemIds,
	 * 	 key : roleId, value : Set<String> roleIds
	 * }
	 */
	private Map<String, Set<String>> findCheckedSystemAndRoleByUserId(String userId) {
		if(StringUtils.isBlank(userId)){
			return null;
		}
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		Map<String, Set<String>> checkedSysRole = roleInfoFacade.findCheckedSystemAndRoleByUserId(userId);
		return checkedSysRole;
	}
	
	private List<SystemRoleVO> converterSysRoleVO(List<SysSystem> sysSystems, List<SystemRoleVO> systemRoleVOs){
		if(systemRoleVOs == null){
			systemRoleVOs = new ArrayList<SystemRoleVO>();
		}
		for (SysSystem sysSystem : sysSystems) {
			systemRoleVOs.add(BeanConvertUtil.convert(sysSystem, null));
		}
		sysSystems = null;
		return systemRoleVOs;
	}

    /**
     * 检查角色名是否重复（同一系统之下）
     * @param rolename
     * @param systemId
     * @return
     */
    public boolean checkRoleNameUnique(String rolename, String systemId) {
        RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
        Map<String, String> param = new HashMap<String, String>();
        param.put("name", rolename);
        List<RoleInfo> roleInfos = roleInfoFacade.findRolesBySystemId(systemId, param);
        return roleInfos.isEmpty();
    }
}
