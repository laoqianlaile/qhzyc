package com.ces.config.dhtmlx.service.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.resource.ResourceDao;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.entity.resource.ResourceButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.systemversion.SystemVersionResourceService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ResourceUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;

/**
 * 资源Service
 * 
 * @author wanglei
 * @date 2015-04-15
 */
@Component("resourceService")
public class ResourceService extends ConfigDefineDaoService<Resource, ResourceDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("resourceDao")
    @Override
    protected void setDaoUnBinding(ResourceDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (非 Javadoc) <p>标题: delete</p> <p>描述: 多个删除并且向下递归</p> @param ids 多个ID用逗号隔开
     * @see com.ces.xarch.core.service.AbstractService#delete(java.io.Serializable)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            Resource resource = getByID(idArray[i]);
            if ("1".equals(resource.getType())) {
                Resource parentResource = getByID(resource.getParentId());
                ResourceUtil.getInstance().removeButtonResource(parentResource.getTargetId(), resource);
                getService(ResourceButtonService.class).deleteByResourceId(idArray[i]);
                getService(SystemVersionResourceService.class).deleteByResourceId(idArray[i]);
            } else {
                deleteByMenuResourceId(idArray[i]);
            }
            getDao().delete(idArray[i]);
        }
    }

    /**
     * 保存菜单资源
     * 
     * @param menu 菜单
     * @return Resource
     */
    public Resource saveMenuResource(Menu menu) {
        Resource parentResource = getByTargetId(menu.getParentId());
        String parentId = parentResource == null ? "-1" : parentResource.getId();
        Resource resource = new Resource();
        resource.setName(menu.getName());
        resource.setParentId(parentId);
        resource.setSystemId(menu.getRootMenuId());
        resource.setTargetId(menu.getId());
        resource.setType("0");
        resource.setCanUse("1");
        Integer maxShowOrder = getMaxShowOrder(parentId);
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        resource.setShowOrder(showOrder);
        return getDao().save(resource);
    }

    /**
     * 保存按钮资源
     * 
     * @param model 资源实体
     * @param buttonIds 资源下的按钮
     * @return Resource
     */
    @Transactional
    public Resource saveButtonResource(Resource model, String buttonIds) {
        if (StringUtil.isEmpty(model)) {
            Integer maxShowOrder = getMaxShowOrder(model.getParentId());
            int showOrder = 0;
            if (maxShowOrder == null) {
                showOrder = 1;
            } else {
                showOrder = maxShowOrder + 1;
            }
            model.setShowOrder(showOrder);
        } else {
            getService(ResourceButtonService.class).deleteByResourceId(model.getId());
        }
        boolean isNew = true;
        if (StringUtil.isNotEmpty(model.getId())) {
            isNew = false;
        }
        model.setType("1");
        Resource resource = super.save(model);
        if (isNew) {
            Resource parentResource = getByID(resource.getParentId());
            ResourceUtil.getInstance().putButtonResource(parentResource.getTargetId(), resource);
        }
        String[] buttonIdArr = buttonIds.split(",");
        if (buttonIdArr != null && buttonIdArr.length > 0) {
            List<ResourceButton> resourceButtonList = new ArrayList<ResourceButton>();
            ResourceButton resourceButton = null;
            Vector<String> constructButtonIdVector = new Vector<String>();
            Vector<String> componentButtonVector = new Vector<String>();
            for (String buttonId : buttonIdArr) {
                String[] strs = buttonId.split("_");
                if (strs.length == 3) {
                    continue;
                }
                if (buttonId.startsWith("CVB_")) {
                    resourceButton = new ResourceButton();
                    resourceButton.setResourceId(resource.getId());
                    resourceButton.setButtonSource("1");
                    resourceButton.setButtonId(buttonId.replace("CVB_", ""));
                    resourceButtonList.add(resourceButton);
                    componentButtonVector.add(buttonId.replace("CVB_", ""));
                } else if (buttonId.startsWith("CDB_")) {
                    resourceButton = new ResourceButton();
                    resourceButton.setResourceId(resource.getId());
                    resourceButton.setButtonSource("0");
                    resourceButton.setButtonId(buttonId.replace("CDB_", ""));
                    resourceButtonList.add(resourceButton);
                    constructButtonIdVector.add(buttonId.replace("CDB_", ""));
                }
            }
            getService(ResourceButtonService.class).save(resourceButtonList);
            ResourceUtil.getInstance().putResourceButtons(resource.getId(), ResourceUtil.CONSTRUCT_BUTTON, constructButtonIdVector);
            ResourceUtil.getInstance().putResourceButtons(resource.getId(), ResourceUtil.PAGE_COMPONENT_BUTTON, componentButtonVector);
        }
        return resource;
    }

    /**
     * 获取资源
     * 
     * @param targetId 对应菜单ID或其他资源ID
     * @return Resource
     */
    public Resource getByTargetId(String targetId) {
        return getDao().getByTargetId(targetId);
    }

    /**
     * 获取资源
     * 
     * @param name 资源名称
     * @param parentId 父资源ID
     * @return Resource
     */
    public Resource getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 获取可用或不可用的资源
     * 
     * @param canUse 是否可用 0：不可用 1：可用
     * @return List<Resource>
     */
    public List<Resource> getByCanUse(String canUse) {
        return getDao().getByCanUse(canUse);
    }

    /**
     * 获取子资源
     * 
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    public List<Resource> getByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }

    /**
     * 获取系统下的资源
     * 
     * @param systemId 系统ID
     * @return List<Resource>
     */
    public List<Resource> getBySystemId(String systemId) {
        List<Resource> resourceList = getDao().getBySystemId(systemId);
        return resourceList;
    }

    /**
     * 获取所有（可用）的菜单资源
     * 
     * @return List<String>
     */
    public List<String> getAllCanUseMenuId() {
        return getDao().getAllCanUseMenuId();
    }

    /**
     * 获取资源下子资源最大显示顺序
     * 
     * @param parentId 父资源ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String parentId) {
        return getDao().getMaxShowOrder(parentId);
    }

    /**
     * 获取所有子孙资源
     * 
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    public List<Resource> getAllChildResource(String parentId) {
        List<Resource> childResourceList = getDao().getByParentId(parentId);
        if (CollectionUtils.isNotEmpty(childResourceList)) {
            int size = childResourceList.size();
            for (int i = 0; i < size; i++) {
                List<Resource> temp = getAllChildResource(childResourceList.get(i).getId());
                if (CollectionUtils.isNotEmpty(temp)) {
                    childResourceList.addAll(temp);
                }
            }
        }
        return childResourceList;
    }

    /**
     * 获取多个资源
     * 
     * @param resourceIds 根据IDs获取资源
     * @return List<Resource>
     */
    public List<Resource> getResourcesByIds(String resourceIds) {
        List<Resource> list = new ArrayList<Resource>();
        if (StringUtil.isNotEmpty(resourceIds)) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
            String hql = "select t from Resource t where t.id in('" + resourceIds.replace(",", "','") + "')";
            list = dao.queryEntityForList(hql, Resource.class);
        }
        return list;
    }

    /**
     * 获取资源树
     * 
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    public List<Resource> getResourceTree(String parentId) {
        List<Resource> resourceList = new ArrayList<Resource>();
        List<Object[]> list = getDao().getResourceTree(parentId);
        if (CollectionUtils.isNotEmpty(list)) {
            Resource resource = null;
            for (Object[] objs : list) {
                resource = new Resource();
                resource.setId(StringUtil.null2empty(objs[0]));
                resource.setName(StringUtil.null2empty(objs[1]));
                resource.setSystemId(StringUtil.null2empty(objs[2]));
                resource.setParentId(StringUtil.null2empty(objs[3]));
                resource.setCanCreateButtonResource(StringUtil.null2zero(objs[4]));
                resource.setType("0");
                resourceList.add(resource);
            }
        }
        return resourceList;
    }

    /**
     * 获取显示顺序范围内的资源
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父资源ID
     * @return List<Resource>
     */
    public List<Resource> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId) {
        return getDao().getByShowOrderBetweenAndParentId(start, end, parentId);
    }

    /**
     * 根据资源类型获取资源
     * 
     * @param type 资源类型
     * @return List<Resource>
     */
    public List<Resource> getByType(String type) {
        return getDao().getByType(type);
    }

    /**
     * 根据对应菜单ID或其他资源ID删除资源
     * 
     * @param targetId 对应菜单ID或其他资源ID
     */
    @Transactional
    public void deleteByTargetId(String targetId) {
        Resource resource = getByTargetId(targetId);
        if (resource != null) {
            delete(resource.getId());
        }
    }

    /**
     * 根据菜单资源ID删除资源
     * 
     * @param menuResourceId 菜单资源ID
     */
    @Transactional
    public void deleteByMenuResourceId(String menuResourceId) {
        Resource menuResource = getByID(menuResourceId);
        List<Resource> buttonResourceList = getByParentId(menuResourceId);
        if (CollectionUtils.isNotEmpty(buttonResourceList)) {
            for (Resource buttonResource : buttonResourceList) {
                getService(ResourceButtonService.class).deleteByResourceId(buttonResource.getId());
                getService(SystemVersionResourceService.class).deleteByResourceId(buttonResource.getId());
                getDao().delete(buttonResource);
            }
        }
        ResourceUtil.getInstance().removeMenuButtonResourceCache(menuResource.getTargetId());
    }

    /**
     * 同步到系统管理平台
     * 
     * @param systemIds 系统IDs
     */
    public synchronized void syncToAuth(String systemIds) {
        String[] systemIdArr = systemIds.split(",");
        if (systemIdArr != null && systemIdArr.length > 0) {
            for (String systemId : systemIdArr) {
                syncSystemToAuth(systemId);
            }
        }
    }

    /**
     * 同步到系统管理平台
     * 
     * @param systemId 系统ID
     */
    private void syncSystemToAuth(String systemId) {
        Menu system = getService(MenuService.class).getByID(systemId);
        // 获取要同步的系统中的资源
        List<Resource> resourceList = getDao().getResListNotWithSysRes(systemId);
        if (CollectionUtils.isEmpty(resourceList)) {
            return;
        }
        // 转换成Map<资源ID,资源>
        Map<String, Resource> resourceMap = new HashMap<String, Resource>();
        for (Resource res : resourceList) {
            resourceMap.put(res.getId(), res);
        }
        // 获取系统管理平台中的资源
        ResourceInfoFacade resourceInfoFacade = CommonUtil.getResourceInfoFacade();
        List<ResourceInfo> authResourceList = null;
        // 没有同步过，需要创建系统
        SystemFacade systemFacade = SystemFacadeFactory.newInstance().createSystemFacade();
        SystemInfo authSystem = systemFacade.findSystemsByCode(systemId);
        String authSystemId = null;
        if (authSystem == null) {
            // 创建系统
            authSystem = new SystemInfo();
            authSystem.setCode(systemId);
            authSystem.setName(system.getName());
            authSystem.setComments(system.getName());
            authSystem.setShowOrder(Long.parseLong(String.valueOf((system.getShowOrder() + 1))));
            authSystem = systemFacade.save(authSystem);
            authSystemId = authSystem.getId();
        } else {
            authSystemId = authSystem.getId();
            if (!authSystem.getName().equals(system.getName())) {
                authSystem.setName(system.getName());
                systemFacade.update(authSystem);
            }
        }
        authResourceList = resourceInfoFacade.findResourcesBySystemId(authSystemId, null);
        if (CollectionUtils.isEmpty(authResourceList)) {
            // 直接同步资源
            Map<String, Map<String, Object>> authResMapMap = new HashMap<String, Map<String, Object>>();
            Map<String, Object> authResMap = null;
            for (Resource res : resourceList) {
                authResMap = new HashMap<String, Object>();
                authResMap.put("resourceId", UUIDGenerator.uuid());
                authResMap.put("name", res.getName());
                authResMap.put("type", res.getType());
                authResMap.put("key", res.getId());
                authResMap.put("parentKey", res.getParentId());
                authResMap.put("showOrder", res.getShowOrder());
                authResMap.put("comments", StringUtil.null2empty(res.getRemark()));
                authResMap.put("sysResId", UUIDGenerator.uuid());
                authResMapMap.put(res.getId(), authResMap);
            }
            String resourceKey = null;
            Map<String, Object> parentAuthResMap = null;
            for (Iterator<String> it = authResMapMap.keySet().iterator(); it.hasNext();) {
                resourceKey = it.next();
                authResMap = authResMapMap.get(resourceKey);
                parentAuthResMap = authResMapMap.get(authResMap.get("parentKey"));
                if (parentAuthResMap == null) {
                    authResMap.put("parentId", "-1");
                } else {
                    authResMap.put("parentId", parentAuthResMap.get("resourceId"));
                }
            }
            List<ResourceInfo> resourceInfoList = new ArrayList<ResourceInfo>();
            ResourceInfo resourceInfo = null;
            for (Iterator<String> it = authResMapMap.keySet().iterator(); it.hasNext();) {
                resourceKey = it.next();
                resourceInfo = new ResourceInfo();
                authResMap = authResMapMap.get(resourceKey);
                resourceInfo.setId(String.valueOf(authResMap.get("resourceId")));
                resourceInfo.setParentId(String.valueOf(authResMap.get("parentId")));
                resourceInfo.setName(String.valueOf(authResMap.get("name")));
                resourceInfo.setResourceTypeName(String.valueOf(authResMap.get("type")));
                resourceInfo.setComments(String.valueOf(authResMap.get("comments")));
                resourceInfo.setResoureceKey(String.valueOf(authResMap.get("key")));
                resourceInfo.setShowOrder(Long.parseLong(String.valueOf(authResMap.get("showOrder"))));
                resourceInfoList.add(resourceInfo);
            }
            resourceInfoFacade.save(resourceInfoList, authSystem.getId());
        } else {
            // 增量同步资源
            // 转换成Map<资源key,资源>
            Map<String, ResourceInfo> authResourceInfoMap = new HashMap<String, ResourceInfo>();
            for (ResourceInfo resInfo : authResourceList) {
                authResourceInfoMap.put(resInfo.getResoureceKey(), resInfo);
            }
            // 新增的资源IDs
            Set<String> newResIdSet = new HashSet<String>();
            // 修改的资源IDs
            Set<String> updateResIdSet = new HashSet<String>();
            // 删除的资源IDs
            Set<String> deleteResIdSet = new HashSet<String>();
            newResIdSet.addAll(resourceMap.keySet());
            newResIdSet.removeAll(authResourceInfoMap.keySet());
            deleteResIdSet.addAll(authResourceInfoMap.keySet());
            deleteResIdSet.removeAll(resourceMap.keySet());
            updateResIdSet.addAll(resourceMap.keySet());
            updateResIdSet.removeAll(newResIdSet);
            updateResIdSet.removeAll(deleteResIdSet);
            // 删除资源
            Entry<String, ResourceInfo> entry = null;
            for (Iterator<Entry<String, ResourceInfo>> it = authResourceInfoMap.entrySet().iterator(); it.hasNext();) {
                entry = it.next();
                if (deleteResIdSet.contains(entry.getKey())) {
                    resourceInfoFacade.delete(entry.getValue().getId());
                    it.remove();
                }
            }
            // 修改资源
            ResourceInfo updateResourceInfo = null;
            for (Resource res : resourceList) {
                if (updateResIdSet.contains(res.getId())) {
                    updateResourceInfo = authResourceInfoMap.get(res.getId());
                    if (updateResourceInfo != null) {
                        updateResourceInfo.setName(res.getName());
                        updateResourceInfo.setShowOrder(Long.parseLong("" + res.getShowOrder()));
                        updateResourceInfo.setComments(StringUtil.null2empty(res.getRemark()));
                        resourceInfoFacade.update(updateResourceInfo);
                    }
                }
            }
            // 新增资源
            Map<String, Map<String, Object>> authResMapMap = new HashMap<String, Map<String, Object>>();
            Map<String, Object> authResMap = null;
            for (Resource res : resourceList) {
                if (newResIdSet.contains(res.getId())) {
                    authResMap = new HashMap<String, Object>();
                    authResMap.put("resourceId", UUIDGenerator.uuid());
                    authResMap.put("name", res.getName());
                    authResMap.put("type", res.getType());
                    authResMap.put("key", res.getId());
                    authResMap.put("parentKey", res.getParentId());
                    authResMap.put("showOrder", res.getShowOrder());
                    authResMap.put("comments", StringUtil.null2empty(res.getRemark()));
                    authResMap.put("sysResId", UUIDGenerator.uuid());
                    authResMapMap.put(res.getId(), authResMap);
                }
            }
            String resourceKey = null;
            Map<String, Object> parentAuthResMap = null;
            ResourceInfo parentAuthResourceInfo = null;
            for (Iterator<String> it = authResMapMap.keySet().iterator(); it.hasNext();) {
                resourceKey = it.next();
                authResMap = authResMapMap.get(resourceKey);
                parentAuthResMap = authResMapMap.get(authResMap.get("parentKey"));
                if (parentAuthResMap == null) {
                    parentAuthResourceInfo = authResourceInfoMap.get(authResMap.get("parentKey"));
                    if (parentAuthResourceInfo != null) {
                        authResMap.put("parentId", parentAuthResourceInfo.getId());
                    } else {
                        authResMap.put("parentId", "-1");
                    }
                } else {
                    authResMap.put("parentId", parentAuthResMap.get("resourceId"));
                }
            }
            List<ResourceInfo> resourceInfoList = new ArrayList<ResourceInfo>();
            ResourceInfo resourceInfo = null;
            for (Iterator<String> it = authResMapMap.keySet().iterator(); it.hasNext();) {
                resourceKey = it.next();
                authResMap = authResMapMap.get(resourceKey);
                if (authResMap.get("parentId") == null) {
                    continue;
                }
                resourceInfo = new ResourceInfo();
                authResMap = authResMapMap.get(resourceKey);
                resourceInfo.setId(String.valueOf(authResMap.get("resourceId")));
                resourceInfo.setParentId(String.valueOf(authResMap.get("parentId")));
                resourceInfo.setName(String.valueOf(authResMap.get("name")));
                resourceInfo.setResourceTypeName(String.valueOf(authResMap.get("type")));
                resourceInfo.setComments(String.valueOf(authResMap.get("comments")));
                resourceInfo.setResoureceKey(String.valueOf(authResMap.get("key")));
                resourceInfo.setShowOrder(Long.parseLong(String.valueOf(authResMap.get("showOrder"))));
                resourceInfoList.add(resourceInfo);
            }
            resourceInfoFacade.save(resourceInfoList, authSystem.getId());
        }
    }
}
