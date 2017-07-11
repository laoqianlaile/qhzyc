package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件前台JS方法参数(供页面构件关闭时使用)实体类
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Entity
@Table(name = "T_XTPZ_COMP_CALLBACK_PARAM")
public class ComponentCallbackParam extends StringIDEntity {

    private static final long serialVersionUID = 1960529201363171446L;

    /** * JS方法ID */
    private String callbackId;

    /** * 返回值中字段名称 */
    private String name;

    /** * 介绍 */
    private String remark;

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
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
