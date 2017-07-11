package com.ces.config.dhtmlx.entity.systemversion;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统版本实体类
 * 
 * @author wanglei
 * @date 2015-04-18
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_VERSION")
public class SystemVersion extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 名称 */
    private String name;

    /** * 是否是默认版本 0：不是 1：是 */
    private String isDefault;

    /** * 系统ID */
    private String systemId;

    /*** 备注 **/
    private String remark;

    /** * 显示顺序 */
    private Integer showOrder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
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