package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件出参实体类
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_OUTPUT_PARAM")
public class ComponentOutputParam extends StringIDEntity {

    private static final long serialVersionUID = -5800801573158319208L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 参数名称 */
    private String name;

    /** * 参数说明 */
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
