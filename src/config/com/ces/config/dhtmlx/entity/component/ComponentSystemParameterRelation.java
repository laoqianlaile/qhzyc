package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件版本中系统参数和系统中系统参数的关联关系实体类
 * 
 * @author wanglei
 * @date 2013-08-20
 */
@Entity
@Table(name = "T_XTPZ_COMP_SYS_PARAM_RELATION")
public class ComponentSystemParameterRelation extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 构件系统参数ID */
    private String componentSystemParamId;

    /** * 系统参数参数ID */
    private String systemParamId;

    /** * 构件版本ID */
    private String componentVersionId;

    public String getComponentSystemParamId() {
        return componentSystemParamId;
    }

    public void setComponentSystemParamId(String componentSystemParamId) {
        this.componentSystemParamId = componentSystemParamId;
    }

    public String getSystemParamId() {
        return systemParamId;
    }

    public void setSystemParamId(String systemParamId) {
        this.systemParamId = systemParamId;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }
}