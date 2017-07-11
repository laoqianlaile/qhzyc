package com.ces.config.dhtmlx.action.component;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentDao;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.component.ComponentService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;

/**
 * 构件Controller
 * 
 * @author wanglei
 * @date 2013-07-22
 */
public class ComponentController extends ConfigDefineServiceDaoController<Component, ComponentService, ComponentDao> {

    private static final long serialVersionUID = -132561819688171083L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Component());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentService")
    @Override
    protected void setService(ComponentService service) {
        super.setService(service);
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        String name = getParameter("name");
        String componentVersionId = getParameter("componentVersionId");
        Component oldComponent = null;
        if (StringUtil.isNotEmpty(componentVersionId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (componentVersion != null) {
                oldComponent = componentVersion.getComponent();
            }
        }
        Component temp = getService().getComponentByName(name);
        boolean nameExist = false;
        if (null != temp) {
            if (oldComponent == null) {
                nameExist = true;
            } else if (!oldComponent.getId().equals(temp.getId())) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields1() {
        String componentVersionId = getParameter("componentVersionId");
        String name = getParameter("name");
        String alias = getParameter("alias");
        String version = getParameter("version");
        boolean updateAlias = false;
        String oldAlias = null;
        boolean versionExist = false;
        boolean typeError = false;
        Component newComponent = getService().getComponentByName(name);
        // 构件版本是否为空，为空则新增，否则为修改
        if (StringUtil.isNotEmpty(componentVersionId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            Component oldComponent = componentVersion.getComponent();
            // 是否修改了构件名称
            if (name.equals(oldComponent.getName())) {
                // 修改了构件别名
                if (!alias.equals(oldComponent.getAlias())) {
                    updateAlias = true;
                    oldAlias = oldComponent.getAlias();
                }
                // 修改了版本号
                if (!version.equals(componentVersion.getVersion())) {
                    ComponentVersion temp = getService(ComponentVersionService.class)
                            .getComponentVersionByComponentIdAndVersion(oldComponent.getId(), version);
                    if (temp != null) {
                        versionExist = true;
                    }
                }
            } else {
                // 修改了构件
                if (newComponent != null) {
                    if (!ConstantVar.Component.Type.ASSEMBLY.equals(newComponent.getType())) {
                        typeError = true;
                    }
                    // 修改了构件别名
                    if (!alias.equals(newComponent.getAlias())) {
                        updateAlias = true;
                        oldAlias = newComponent.getAlias();
                    }
                    ComponentVersion temp = getService(ComponentVersionService.class)
                            .getComponentVersionByComponentIdAndVersion(newComponent.getId(), version);
                    if (temp != null) {
                        versionExist = true;
                    }
                }
            }
        } else {
            // 修改了构件
            if (newComponent != null) {
                if (!ConstantVar.Component.Type.ASSEMBLY.equals(newComponent.getType())) {
                    typeError = true;
                }
                // 修改了构件别名
                if (!alias.equals(newComponent.getAlias())) {
                    updateAlias = true;
                    oldAlias = newComponent.getAlias();
                }
                ComponentVersion temp = getService(ComponentVersionService.class)
                        .getComponentVersionByComponentIdAndVersion(newComponent.getId(), version);
                if (temp != null) {
                    versionExist = true;
                }
            }
        }
        setReturnData("{'typeError':" + typeError + ",'updateAlias':" + updateAlias + ",'oldAlias':'"
                + StringUtil.null2empty(oldAlias) + "','versionExist':" + versionExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
