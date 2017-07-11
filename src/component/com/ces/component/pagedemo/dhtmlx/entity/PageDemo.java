package com.ces.component.pagedemo.dhtmlx.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_COMP_PAGE")
public class PageDemo extends StringIDEntity {

    private static final long serialVersionUID = 2395023253556736515L;

    /** * 类别ID */
    private String moduleId;

    /** * 名称 */
    private String name;

    /** * 值 */
    private String value;

    /** * 说明 */
    private String remark;

    /** * 显示顺序 */
    private Integer showOrder;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

}
