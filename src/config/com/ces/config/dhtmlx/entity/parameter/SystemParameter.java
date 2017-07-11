package com.ces.config.dhtmlx.entity.parameter;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统参数实体类
 * 
 * @author wanglei
 * @date 2013-08-12
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_PARAM")
public class SystemParameter extends StringIDEntity {

    private static final long serialVersionUID = 2395023253556736515L;

    /** * 类别ID */
    private String categoryId;

    /** * 参数名称 */
    private String name;

    /** * 参数默认值 */
    private String value;

    /** * 参数类型 3：PDT 2：实施人员 1：用户 */
    private String type;

    /** * 参数说明 */
    private String remark;

    /** * 显示顺序 */
    private Integer showOrder;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

}
