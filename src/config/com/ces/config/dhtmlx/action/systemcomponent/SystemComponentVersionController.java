package com.ces.config.dhtmlx.action.systemcomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.systemcomponent.SystemComponentVersionDao;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.systemcomponent.SystemComponentVersion;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.systemcomponent.SystemComponentVersionService;
import com.ces.config.utils.ConstantVar;

/**
 * 系统中直接绑定的构件（非通过菜单方式）Controller
 * 
 * @author wanglei
 * @date 2014-04-24
 */
public class SystemComponentVersionController extends
        ConfigDefineServiceDaoController<SystemComponentVersion, SystemComponentVersionService, SystemComponentVersionDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemComponentVersion());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("systemComponentVersionService")
    @Override
    protected void setService(SystemComponentVersionService service) {
        super.setService(service);
    }

    /**
     * 获取系统下所有的构件
     * 
     * @return Object
     */
    public Object getSystemComponentVersions() {
        String menuId = getParameter("menuId");
        List<Object[]> returnDataList = new ArrayList<Object[]>();
        Map<String, Set<ComponentVersion>> componentVersionMap = getService(MenuService.class).getComponentVersionMapByMenuId(menuId);
        if (MapUtils.isNotEmpty(componentVersionMap)) {
            Set<ComponentVersion> componentVersionSet = componentVersionMap.get("-1");
            parseComponentVersionSet(componentVersionMap, componentVersionSet, returnDataList, 0);
        }
        List<SystemComponentVersion> systemComponentVersionList = getService().getByRootMenuId(menuId);
        if (CollectionUtils.isNotEmpty(returnDataList)) {
            Object[] objects = null;
            for (SystemComponentVersion systemComponentVersion : systemComponentVersionList) {
                objects = new Object[6];
                objects[0] = systemComponentVersion.getId();
                objects[1] = systemComponentVersion.getComponentVersion().getComponent().getAlias();
                objects[2] = systemComponentVersion.getComponentVersion().getComponent().getName();
                objects[3] = systemComponentVersion.getComponentVersion().getVersion();
                objects[4] = systemComponentVersion.getComponentVersion().getComponent().getType();
                objects[5] = "0";
                returnDataList.add(objects);
            }
        }
        setReturnData(returnDataList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    private void parseComponentVersionSet(Map<String, Set<ComponentVersion>> componentVersionMap, Set<ComponentVersion> componentVersionSet,
            List<Object[]> returnDataList, int level) {
        Object[] objects = null;
        if (CollectionUtils.isNotEmpty(componentVersionSet)) {
            for (ComponentVersion componentVersion : componentVersionSet) {
                objects = new Object[6];
                objects[0] = "SCV_" + componentVersion.getId();
                objects[1] = getPrefixSpace(level) + componentVersion.getComponent().getAlias();
                objects[2] = componentVersion.getComponent().getName();
                objects[3] = componentVersion.getVersion();
                objects[4] = componentVersion.getComponent().getType();
                objects[5] = "1";
                returnDataList.add(objects);
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    Set<ComponentVersion> childComponentVersionSet = componentVersionMap.get(componentVersion.getId());
                    parseComponentVersionSet(componentVersionMap, childComponentVersionSet, returnDataList, level + 1);
                }
            }
        }
    }

    /**
     * 获取层级的缩进前缀
     * 
     * @return String
     */
    private String getPrefixSpace(int level) {
        String str = "";
        for (int i = 0; i < level; i++)
            str += "　  ";
        return str;
    }

    /**
     * 校验添加的构件和原先系统中的构件是否有冲突
     * 
     * @return Object
     */
    public Object validateSystemComponentVersion() {
        String componentVersionIds = getParameter("componentVersionIds");
        String rootMenuId = getParameter("rootMenuId");
        String[] componentVersionIdArray = componentVersionIds.split(",");
        List<ComponentVersion> selectedComponentVersionList = new ArrayList<ComponentVersion>();
        if (componentVersionIdArray.length > 0) {
            ComponentVersion componentVersion = null;
            for (String componentVersionId : componentVersionIdArray) {
                componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                if (componentVersion != null) {
                    selectedComponentVersionList.add(componentVersion);
                }
            }
        }
        // 取得根菜单下的所有构件版本ID
        Set<ComponentVersion> rootMenuComponentVersionSet = getService(MenuService.class).getComponentVersionByRootMenuId(rootMenuId);
        boolean flag = true;
        StringBuilder failureComponentVersionId = new StringBuilder();
        if (CollectionUtils.isNotEmpty(rootMenuComponentVersionSet)) {
            for (ComponentVersion rootMenuComponentVersion : rootMenuComponentVersionSet) {
                for (ComponentVersion componentVersion : selectedComponentVersionList) {
                    if (rootMenuComponentVersion.getComponent().getId().equals(componentVersion.getComponent().getId())) {
                        flag = false;
                        failureComponentVersionId.append(componentVersion.getId() + ",");
                    }
                }
            }
        }
        if (!flag) {
            failureComponentVersionId.deleteCharAt(failureComponentVersionId.length() - 1);
        }
        setReturnData("{'success':" + flag + ",'failureComponentVersionId':'" + failureComponentVersionId + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 添加构件
     * 
     * @return Object
     */
    public Object saveSystemComponentVersion() {
        String componentVersionIds = getParameter("componentVersionIds");
        String rootMenuId = getParameter("rootMenuId");
        String[] componentVersionIdArray = componentVersionIds.split(",");
        getService().saveSystemComponentVersion(rootMenuId, componentVersionIdArray);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
