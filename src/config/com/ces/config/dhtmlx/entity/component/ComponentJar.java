package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件中的jar文件实体类
 * 
 * @author wanglei
 * @date 2013-08-09
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_JAR")
public class ComponentJar extends StringIDEntity {

    private static final long serialVersionUID = -5911494088218601660L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * jar文件名称 */
    private String name;

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

}
