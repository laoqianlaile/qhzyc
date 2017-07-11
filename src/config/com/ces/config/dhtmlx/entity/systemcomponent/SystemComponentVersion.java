package com.ces.config.dhtmlx.entity.systemcomponent;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统中直接绑定的构件（非通过菜单方式）实体
 * 
 * @author wanglei
 * @date 2014-04-24
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_COMPONENT")
public class SystemComponentVersion extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 系统ID */
    private String rootMenuId;

    /** * 绑定的构件类型 */
    private ComponentVersion componentVersion;

    /** * 是否绑定菜单 0：没绑定 1：绑定 */
    private String bindingMenu = "0";

    public String getRootMenuId() {
        return rootMenuId;
    }

    public void setRootMenuId(String rootMenuId) {
        this.rootMenuId = rootMenuId;
    }

    @OneToOne
    @JoinColumn(name = "COMPONENT_VERSION_ID")
    public ComponentVersion getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(ComponentVersion componentVersion) {
        this.componentVersion = componentVersion;
    }

    @Transient
    public String getBindingMenu() {
        return bindingMenu;
    }

    public void setBindingMenu(String bindingMenu) {
        this.bindingMenu = bindingMenu;
    }

}