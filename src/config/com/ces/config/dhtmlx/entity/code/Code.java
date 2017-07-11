package com.ces.config.dhtmlx.entity.code;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 编码实体类
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Entity
@Table(name = "T_XTPZ_CODE")
public class Code extends StringIDEntity implements Comparable<Code> {

    private static final long serialVersionUID = -6518345568122895195L;

    /** * 编码名称 */
    private String name;

    /** * 编码值 */
    private String value;

    /** * 显示顺序 */
    private Integer showOrder;

    /** * 编码类型编码 */
    private String codeTypeCode;

    /** * 父编码ID */
    private String parentId;

    /** * 备注 */
    private String remark;

    /** * 最值顺序 */
    private Integer mostValueShowOrder;

    /** * 是否是系统编码 0:不是 1:是 */
    private String isSystem;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getCodeTypeCode() {
        return codeTypeCode;
    }

    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getMostValueShowOrder() {
        return mostValueShowOrder;
    }

    public void setMostValueShowOrder(Integer mostValueShowOrder) {
        this.mostValueShowOrder = mostValueShowOrder;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public int compareTo(Code o) {
        int result = 0;
        if (this.showOrder.intValue() > o.showOrder.intValue()) {
            result = 1;
        } else if (this.showOrder.intValue() < o.showOrder.intValue()) {
            result = -1;
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codeTypeCode == null) ? 0 : codeTypeCode.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        final Code other = (Code) obj;
        if (codeTypeCode == null) {
            if (other.codeTypeCode != null)
                return false;
        } else if (!codeTypeCode.equals(other.codeTypeCode))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
