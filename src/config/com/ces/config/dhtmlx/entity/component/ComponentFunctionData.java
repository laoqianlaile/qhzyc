package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件前台JS方法返回值实体类
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_FUNCTION_DATA")
public class ComponentFunctionData extends StringIDEntity {

    private static final long serialVersionUID = 1960529201363171446L;

    /** * JS方法ID */
    private String functionId;

    /** * 返回值中字段名称 */
    private String name;

    /** * 介绍 */
    private String remark;

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
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
