package com.ces.config.dhtmlx.entity.component;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件相关表实体类
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_TABLE")
public class ComponentTable extends StringIDEntity {

    private static final long serialVersionUID = 6419922995036017255L;

    /** * 构件表表名 */
    private String name;

    /** * 是否是自定义的表，如果是自定义的表，那么columns和release_with_data不要了，直接在表定义里定义 0-不是 1-是 */
    private String isSelfdefine;

    /** * 发布时是否生成数据脚本 0-不生成，1-生成 */
    private String releaseWithData;

    /** * 构件表中的列 */
    private List<ComponentColumn> componentColumnList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSelfdefine() {
        return isSelfdefine;
    }

    public void setIsSelfdefine(String isSelfdefine) {
        this.isSelfdefine = isSelfdefine;
    }

    public String getReleaseWithData() {
        return releaseWithData;
    }

    public void setReleaseWithData(String releaseWithData) {
        this.releaseWithData = releaseWithData;
    }

    @Transient
    public List<ComponentColumn> getComponentColumnList() {
        return componentColumnList;
    }

    public void setComponentColumnList(List<ComponentColumn> componentColumnList) {
        this.componentColumnList = componentColumnList;
    }

}
