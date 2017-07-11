package com.ces.config.dhtmlx.entity.systemversion;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统版本与资源关联关系实体类
 * 
 * @author wanglei
 * @date 2015-04-18
 */
@Entity
@Table(name = "T_XTPZ_SYS_VERSION_RESOURCE")
public class SystemVersionResource extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 系统版本ID */
    private String systemVersionId;

    /** * 资源ID */
    private String resourceId;

    public String getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(String systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

}