package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 逻辑表组和逻辑表关系表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_LOGIC_GROUP_RELATION")
public class LogicGroupRelation extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 逻辑表组CODE */
    private String groupCode;

    /** 逻辑表CODE */
    private String tableCode;

    /** 逻辑表对应父逻辑表 */
    private String parentTableCode;

    /** 显示顺序 */
    private Integer showOrder;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getParentTableCode() {
        return parentTableCode;
    }

    public void setParentTableCode(String parentTableCode) {
        this.parentTableCode = parentTableCode;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

}
