package com.ces.config.dhtmlx.entity.resource;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 按钮资源与实际按钮关系实体类
 * 
 * @author wanglei
 * @date 2015-04-15
 */
@Entity
@Table(name = "T_XTPZ_RESOURCE_BUTTON")
public class ResourceButton extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 资源ID */
    private String resourceId;

    /** * 按钮ID */
    private String buttonId;

    /** * 按钮来源 0：组装按钮 1：开发构件按钮 */
    private String buttonSource;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonSource() {
        return buttonSource;
    }

    public void setButtonSource(String buttonSource) {
        this.buttonSource = buttonSource;
    }

}