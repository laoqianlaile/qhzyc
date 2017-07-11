package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件相关表的字段实体类
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_COLUMN")
public class ComponentColumn extends StringIDEntity {

    private static final long serialVersionUID = 7062189569973551273L;

    /** * 构件表字段名 */
    private String name;

    /** * 字段类型 */
    private String type;

    /** * 字段长度 */
    private Integer length;

    /** * 字段是否可为空 */
    private String isNull;

    /** * 字段默认值 */
    private String defaultValue;

    /** * 字段介绍 */
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        final ComponentColumn other = (ComponentColumn) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
