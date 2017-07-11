package com.ces.config.dhtmlx.entity.resource;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 资源实体类
 * 
 * @author wanglei
 * @date 2015-04-15
 */
@Entity
@Table(name = "T_XTPZ_RESOURCE")
public class Resource extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 名称 */
    private String name;

    /** * 类型 0：菜单 1：按钮 */
    private String type;

    /** * 父资源ID */
    private String parentId;

    /** * 系统ID */
    private String systemId;

    /** * 对应菜单ID或其他资源ID */
    private String targetId;

    /** * 显示顺序 */
    private int showOrder;

    /*** 备注 **/
    private String remark;

    /*** 是否允许创建按钮资源（该资源为菜单资源） **/
    private String canCreateButtonResource = "0";

    /*** 是否可用 0：不可用 1：可用 配置系统中都是可用的，发布时根据版本中资源情况来配置是否可用 **/
    private String canUse;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public String getCanCreateButtonResource() {
        return canCreateButtonResource;
    }

    public void setCanCreateButtonResource(String canCreateButtonResource) {
        this.canCreateButtonResource = canCreateButtonResource;
    }

    public String getCanUse() {
        return canUse;
    }

    public void setCanUse(String canUse) {
        this.canUse = canUse;
    }

    @Transient
    public Boolean getHasChild() {
        return "0".equals(type);
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
        Resource other = (Resource) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}