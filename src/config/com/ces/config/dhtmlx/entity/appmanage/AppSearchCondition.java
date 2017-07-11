package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * <p>描述: 高级检索中保存的用户查询条件</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-2-28 下午5:19:59
 *
 */
@Entity
@Table(name = "T_XTPZ_APP_SEARCH_CONDITION")
public class AppSearchCondition extends StringIDEntity {

    private static final long serialVersionUID = -2094904769529809580L;
    // 表ID
    private String tableId;
    //
    private String componentVersionId;
    // 用户ID
    private String userId;
    // 检索条件名称
    private String name;
    // 检索条件内容
    private String condition;
    // 时间戳
    private long timestamp;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }
    
    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
