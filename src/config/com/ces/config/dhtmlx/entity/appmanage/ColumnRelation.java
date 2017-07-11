package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
@Entity
@Table(name = "T_XTPZ_COLUMN_RELATION")
public class ColumnRelation extends StringIDEntity {
    
    private static final long serialVersionUID = -5786499050809576922L;
    
    public static final String SPLICE = "0";   // 字段拼接 
    public static final String SPLIT  = "1";   // 字段截取
    public static final String INHERIT = "2";  // 继承
    public static final String SUM    = "3";   // 求和
    public static final String MOST   = "4";   // 最值计算
    public static final String BUSINESS = "5"; // 特殊业务

    /* 关联表ID */
    private String tableId;

    /* 名称 */
    private String name;

    /* 类型：0-字段拼接 1-字段截取 2-继承 3-求和 4-最值计算 5-特殊业务 */
    private String type;
    
    /* 显示顺序* */
	private Integer showOrder;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

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

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

}
