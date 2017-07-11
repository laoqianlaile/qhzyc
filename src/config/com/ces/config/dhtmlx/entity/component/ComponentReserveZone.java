package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件预留区实体类
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_RESERVE_ZONE")
public class ComponentReserveZone extends StringIDEntity {

    private static final long serialVersionUID = 1608830093284986281L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 页面上的元素的id */
    private String name;

    /** * 别名 */
    private String alias;

    /** * 类型 0:工具条 1:列表超链接 2:按钮预留区 3：树节点预留区 4：标签页预留区 */
    private String type;

    /** * 所在页面 */
    private String page;

    /** * 是否共用预留区：0-非共用 1-共用 */
    private Boolean isCommon;

    /** 显示顺序 */
    private Integer showOrder;

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

}
