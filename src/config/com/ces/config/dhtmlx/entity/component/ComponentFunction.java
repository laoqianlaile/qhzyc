package com.ces.config.dhtmlx.entity.component;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件前台JS方法实体类
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_FUNCTION")
public class ComponentFunction extends StringIDEntity {

    private static final long serialVersionUID = 3068461885831847517L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 方法名称 */
    private String name;

    /** * 所在页面 */
    private String page;

    /** * 介绍 */
    private String remark;

    /** * JS方法返回值中的字段名称 */
    private List<ComponentFunctionData> componentFunctionDataList;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public List<ComponentFunctionData> getComponentFunctionDataList() {
        return componentFunctionDataList;
    }

    public void setComponentFunctionDataList(List<ComponentFunctionData> componentFunctionDataList) {
        this.componentFunctionDataList = componentFunctionDataList;
    }

}
