package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 物理表实体类
 * 
 * @author qiujinwei
 * @date 2014-11-19
 */
@Entity
@Table(name = "T_XTPZ_PHYSICAL_TABLE_DEFINE")
public class PhysicalTableDefine extends StringIDEntity implements Comparable<PhysicalTableDefine> {

    private static final long serialVersionUID = 1L;

    /** 表分类树ID * */
    private String tableTreeId;

    /** 表类型：0-物理表，1-视图 * */
    private String tableType;

    /**
     * 物理表类型：（来源T_XTPZ_TABLE_CLASSIFICATION.CODE）A-档案类型表D-自定义业务表T-模板表（配置平台废弃）
     * P-预置业务表V-视图（配置平台废弃-使用TABLE_TYPE=1）H-层次模版（档案废弃-用逻辑表组代替） *
     */
    private String classification;

    /** 中文显示名称 * */
    private String showName;

    /** 物理表前缀 * */
    private String tablePrefix;

    /** 物理表代码或逻辑表代码 * */
    private String tableCode;

    /** 物理表英文名称全名(TABLE_PREFIX + TABLE_CODE) * */
    private String tableName;

    /** 物理表对应的逻辑表代码（TABLE_TYPE=0时才有对应逻辑表代码）(如果为视图，此字段对应关系表ID) * */
    private String logicTableCode;

    /** 是否已建物理表：0-否，1-是 * */
    private String created;

    /** 显示顺序 * */
    private Integer showOrder;

    /** * 发布时是否生成数据脚本 0-不生成，1-生成 */
    private String releaseWithData;

    /** * 是否创建索引库 0-不建索引库，1-创建索引库 */
    private String createIndex = "0";

    /** 备注 * */
    private String remark;

    public String getTableTreeId() {
        return tableTreeId;
    }

    public void setTableTreeId(String tableTreeId) {
        this.tableTreeId = tableTreeId;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getTableCode() {
        return StringUtil.null2empty(tableCode).trim();
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableName() {
        tableName = StringUtil.null2empty(tablePrefix).trim() + StringUtil.null2empty(tableCode).trim();
        return tableName.toUpperCase();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLogicTableCode() {
        return logicTableCode;
    }

    public void setLogicTableCode(String logicTableCode) {
        this.logicTableCode = logicTableCode;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getReleaseWithData() {
        return releaseWithData;
    }

    public void setReleaseWithData(String releaseWithData) {
        this.releaseWithData = releaseWithData;
    }

    public String getCreateIndex() {
		return createIndex;
	}

	public void setCreateIndex(String createIndex) {
		this.createIndex = createIndex;
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
        result = prime * result + ((tableCode == null) ? 0 : tableCode.hashCode());
        result = prime * result + ((tablePrefix == null) ? 0 : tablePrefix.hashCode());
        result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
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
        PhysicalTableDefine other = (PhysicalTableDefine) obj;
        if (tableCode == null) {
            if (other.tableCode != null)
                return false;
        } else if (!tableCode.equals(other.tableCode))
            return false;
        if (tablePrefix == null) {
            if (other.tablePrefix != null)
                return false;
        } else if (!tablePrefix.equals(other.tablePrefix))
            return false;
        if (tableType == null) {
            if (other.tableType != null)
                return false;
        } else if (!tableType.equals(other.tableType))
            return false;
        return true;
    }

    @Override
    public int compareTo(PhysicalTableDefine o) {
        if (o == null || o.getShowOrder() == null) {
            return 1;
        }
        if (this.getShowOrder() == null) {
            return -1;
        }
        if (this.getShowOrder().intValue() > o.getShowOrder().intValue()) {
            return 1;
        } else if (this.getShowOrder().intValue() < o.getShowOrder().intValue()) {
            return -1;
        } else {
            return 0;
        }
    }

}
