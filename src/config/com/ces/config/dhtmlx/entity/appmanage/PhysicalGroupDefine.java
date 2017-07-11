package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 物理表组定义表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_PHYSICAL_GROUP_DEFINE")
public class PhysicalGroupDefine extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 物理表组名称 */
    private String groupName;

    /** 物理表组编码 */
    private String code;

    /** 物理表组所属逻辑表组CODE */
    private String logicGroupCode;

    /** 显示顺序 */
    private Integer showOrder;

    /** 备注 */
    private String remark;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogicGroupCode() {
        return logicGroupCode;
    }

    public void setLogicGroupCode(String logicGroupCode) {
        this.logicGroupCode = logicGroupCode;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        PhysicalGroupDefine other = (PhysicalGroupDefine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
