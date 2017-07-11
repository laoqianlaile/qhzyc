package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据权限待审批实体类（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_APPROVE")
public class AuthorityApprove extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID. */
    private String objectId;

    /** 类型：0-角色 1-用户 */
    private String objectType;

    /** 对应菜单ID **/
    private String menuId;

    /** 构件版本Id */
    private String componentVersionId;

    /** 待审批的权限类型 1-树权限 2-数据权限 3-编码权限 */
    private String authorityType;

    /** 关联的权限ID */
    private String relateAuthId;

    /** 待审批的权限详情 */
    private String detail;

    /** 操作类型 1-新增 2-修改 3-删除 */
    private String operate;

    /** 状态 0-待审批 1-审批通过 2-审批退回 */
    private String status;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getRelateAuthId() {
        return relateAuthId;
    }

    public void setRelateAuthId(String relateAuthId) {
        this.relateAuthId = relateAuthId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}