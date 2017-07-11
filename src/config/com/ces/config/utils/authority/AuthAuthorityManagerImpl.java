package com.ces.config.utils.authority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.exception.SystemFacadeException;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ResourceUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.security.entity.SysUser;

/**
 * 采用系统管理平台的权限管理实现类
 * 
 * @author wanglei
 * @date 2014-12-16
 */
public class AuthAuthorityManagerImpl extends DefaultAuthorityManagerImpl {

    private static final long serialVersionUID = -4528331047576032537L;

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#getMenuIds()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getMenuIds(String systemId) {
        SysUser user = CommonUtil.getUser();
        List<String> resourceList = new ArrayList<String>();
        if (StringUtil.isEmpty(systemId)) {
            // 取当前登录人员的拥有的角色的菜单权限
            try {
                List<ResourceInfo> resourceInfoList = CommonUtil.getResourceInfoFacade().getResrouceByUserId(user.getId());
                if (CollectionUtils.isNotEmpty(resourceInfoList)) {
                    for (ResourceInfo resourceInfo : resourceInfoList) {
                        resourceList.add(resourceInfo.getResoureceKey());
                    }
                }
            } catch (SystemFacadeException e) {
                System.out.println("获取资源权限错误！");
                e.printStackTrace();
            }

        } else {
            // 取当前登录人员的拥有的角色的菜单权限
            List<ResourceInfo> resourceInfoList = CommonUtil.getResourceInfoFacade().findResourceByUserIdAndSystemCode(user.getId(), systemId);
            if (CollectionUtils.isNotEmpty(resourceInfoList)) {
                for (ResourceInfo resourceInfo : resourceInfoList) {
                    resourceList.add(resourceInfo.getResoureceKey());
                }
            }
        }
        List<String> menuIdList = null;
        if (CollectionUtils.isNotEmpty(resourceList)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < resourceList.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append("'").append(resourceList.get(i)).append("'");
            }
            String menuIdSql = "select t.target_id from t_xtpz_resource t where t.id in (" + sb.toString() + ")";
            menuIdList = DatabaseHandlerDao.getInstance().queryForList(menuIdSql);
        }
        return menuIdList;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedConstructButtonIds(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedConstructButtonIds(String menuId, String componentVersionId) {
        return notUsedButtonIds(menuId, ResourceUtil.CONSTRUCT_BUTTON);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedPageComponentButtonIds(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedPageComponentButtonIds(String menuId, String componentVersionId) {
        return notUsedButtonIds(menuId, ResourceUtil.PAGE_COMPONENT_BUTTON);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.authority.AuthorityManager#notUsedReportIds(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<String> notUsedReportIds(String menuId, String componentVersionId, String tableId) {
        return null;
    }

    /**
     * 获取没有权限的按钮
     * 
     * @param menuId 菜单Id
     * @param buttonSource 按钮来源
     * @return List<String>
     */
    private List<String> notUsedButtonIds(String menuId, String buttonSource) {
        List<String> notUsedButtonIdList = new ArrayList<String>();
        if (CommonUtil.isSuperRole()) {
            return notUsedButtonIdList;
        }
        // 没有权限的按钮 = 系统配置平台该菜单下的按钮资源 - 系统管理平台中有权限的按钮资源
        List<Resource> notUsedResourceList = new ArrayList<Resource>();
        // 获取系统配置平台该菜单下的按钮资源
        List<Resource> xptzButtonResourceList = ResourceUtil.getInstance().getButtonResources(menuId);
        if (CollectionUtils.isNotEmpty(xptzButtonResourceList)) {
            // 获取系统管理平台中有权限的按钮资源
            List<String> authButtonResourceList = getAuthResourceList(menuId);
            notUsedResourceList.addAll(xptzButtonResourceList);
            if (CollectionUtils.isNotEmpty(authButtonResourceList)) {
                for (Iterator<Resource> it = notUsedResourceList.iterator(); it.hasNext();) {
                    if (authButtonResourceList.contains(it.next().getId())) {
                        it.remove();
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(notUsedResourceList)) {
                List<String> resourceButtonList = null;
                for (Resource resource : notUsedResourceList) {
                    resourceButtonList = ResourceUtil.getInstance().getResourceButtons(resource.getId(), buttonSource);
                    if (CollectionUtils.isNotEmpty(resourceButtonList)) {
                        notUsedButtonIdList.addAll(resourceButtonList);
                    }
                }
            }
        }
        // 没有权限的按钮还有加上 被版本过滤掉的的按钮资源
        List<Resource> cannotUseButtonResourceList = ResourceUtil.getInstance().getCannotUseButtonResources(menuId);
        if (CollectionUtils.isNotEmpty(cannotUseButtonResourceList)) {
            List<String> resourceButtonList = null;
            for (Resource resource : cannotUseButtonResourceList) {
                resourceButtonList = ResourceUtil.getInstance().getResourceButtons(resource.getId(), buttonSource);
                if (CollectionUtils.isNotEmpty(resourceButtonList)) {
                    notUsedButtonIdList.addAll(resourceButtonList);
                }
            }
        }
        return notUsedButtonIdList;
    }

    /**
     * 获取系统管理平台中有权限的按钮资源
     * 
     * @param menuId 菜单ID
     * @return List<String>
     */
    private List<String> getAuthResourceList(String menuId) {
        SysUser user = CommonUtil.getUser();
        List<String> list = new ArrayList<String>();
        // 取当前登录人员的拥有的角色的在该菜单下的按钮权限
        List<ResourceInfo> resourceInfoList = CommonUtil.getResourceInfoFacade().findResourceByUserIdAndResKey(user.getId(), menuId);
        if (CollectionUtils.isNotEmpty(resourceInfoList)) {
            for (ResourceInfo resourceInfo : resourceInfoList) {
                list.add(resourceInfo.getResoureceKey());
            }
        }
        return list;
    }
}
