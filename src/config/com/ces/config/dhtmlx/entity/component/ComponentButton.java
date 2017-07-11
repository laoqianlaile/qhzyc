package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件按钮实体类
 * 
 * @author wanglei
 * @date 2014-07-30
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_BUTTON")
public class ComponentButton extends StringIDEntity {

    private static final long serialVersionUID = 7268330959550434235L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 按钮名称 */
    private String name;

    /** * 按钮显示名称 */
    private String displayName;

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
