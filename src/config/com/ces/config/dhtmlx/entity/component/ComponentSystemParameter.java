package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件版本中系统参数实体类
 * 
 * @author wanglei
 * @date 2013-08-20
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_SYSTEM_PARAM")
public class ComponentSystemParameter extends StringIDEntity {

    private static final long serialVersionUID = 3204438017417863844L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 参数名称（唯一的） */
    private String name;

    /** * 参数介绍 */
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
