package com.ces.config.dhtmlx.entity.release;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统发布详情实体类
 * 
 * @author wanglei
 * @date 2015-1-30
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_RELEASE_DETAIL")
public class ReleaseDetail extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 菜单名称 */
    private String name;

    /** * 节点ID */
    private String nodeId;

    /** * 父节点ID */
    private String parentNodeId;

    /** * 数据ID（菜单ID、预留区ID、构件绑定关系ID） */
    private String dataId;

    /** * 系统发布ID */
    private String systemReleaseId;

    /** * 系统ID */
    private String rootMenuId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getSystemReleaseId() {
        return systemReleaseId;
    }

    public void setSystemReleaseId(String systemReleaseId) {
        this.systemReleaseId = systemReleaseId;
    }

    public String getRootMenuId() {
        return rootMenuId;
    }

    public void setRootMenuId(String rootMenuId) {
        this.rootMenuId = rootMenuId;
    }

    @Transient
    public Boolean getHasChild() {
        return true;
    }
}