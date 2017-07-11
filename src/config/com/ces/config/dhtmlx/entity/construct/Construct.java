package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 组合构件绑定关系实体类
 * 
 * @author wanglei
 * @date 2013-08-26
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT")
public class Construct extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 组合构件版本 */
    private ComponentVersion assembleComponentVersion;

    /** * 基础构件版本ID */
    private String baseComponentVersionId;

    @OneToOne
    @JoinColumn(name = "COMPONENT_VERSION_ID")
    public ComponentVersion getAssembleComponentVersion() {
        return assembleComponentVersion;
    }

    public void setAssembleComponentVersion(ComponentVersion assembleComponentVersion) {
        this.assembleComponentVersion = assembleComponentVersion;
    }

    public String getBaseComponentVersionId() {
        return baseComponentVersionId;
    }

    public void setBaseComponentVersionId(String baseComponentVersionId) {
        this.baseComponentVersionId = baseComponentVersionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Construct other = (Construct) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}