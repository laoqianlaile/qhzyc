package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 公用构件依赖关系实体类
 * 
 * @author wanglei
 * @date 2014-06-09
 */
@Entity
@Table(name = "T_XTPZ_COMMOM_COMP_RELATION")
public class CommonComponentRelation extends StringIDEntity {

    private static final long serialVersionUID = 4316468040712502347L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 关联的公用构件版本ID */
    private String commonComponentVersionId;

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    @Column(name = "COMMON_COMP_VERSION_ID")
    public String getCommonComponentVersionId() {
        return commonComponentVersionId;
    }

    public void setCommonComponentVersionId(String commonComponentVersionId) {
        this.commonComponentVersionId = commonComponentVersionId;
    }

}