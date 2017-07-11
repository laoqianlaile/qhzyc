package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_TREE_DEFINE")
public class TreeDefine extends StringIDEntity implements Cloneable{
	
    private static final long serialVersionUID = 4523098751964579977L;
    
    public static final String T_ROOT   = "0"; // 根节点
    public static final String T_EMPTY  = "1"; // 空节点
    public static final String T_TABLE  = "2"; // 表节点
    public static final String T_COLUMN_TAB = "3"; // 本表字段节点
    public static final String T_COLUMN_EMP = "4"; // 跨表字段节点
    public static final String T_GROUP      = "5"; // 物理表组节点
    public static final String T_COFLOW = "6"; // 工作流节点
    public static final String T_LOGIC_GROUP= "7"; // 逻辑表组节点
    public static final String T_BOX    = "9"; // 工作箱
    
    public static final String T_ASC    = "asc";  //升序
    public static final String T_DESC   = "desc"; //降序
    
    public static final String L_1C = "3"; // 整张页面
    public static final String L_2E = "2"; // 上下结构
    public static final String L_2U = "1"; // 左右结构
    public static final String L_3L = "4"; // 左上下结构
    public static final String TAB_PREFIX = "#T"; // 表节点/物理表组columnNames、columnValues值前缀
    public static final String EMP_PREFIX = "#E"; // 根节点、空节点、工作流节点columnNames、columnValues值前缀
    public static final String V_RULE = "#D"; // 动态节点columnNames、columnValues值

    public static final String RULE_STATIC   = "0"; // 静态动态节点
    public static final String RULE_TRIGGER  = "1"; // 触发器动态节点
    public static final String RULE_REALTIME = "2"; // 实时动态节点
    /** 节点显示名称 * */
	private String name;
	/** 父节点ID * */
	private String parentId;
	/** 节点类型（0-根节点，1-空节点，2- 物理表节点，3-本表字段节点，4-跨表字段节点，5-物理表组节点，6-工作流节点，9-工作箱节点） * */
	private String type;
	/** 显示顺序 * */
	private Integer showOrder;
	/** 分类值 * */
	private String value;
	/** ID：当节点分类值为1（表）是表ID,当节点分类值为 2（字段）为字段ID,当节点分类值为 3是字段标签CODE,当节点分类值为5是表组ID,当节点分类值为6是 工作流流程ID* */
	private String dbId;
	/** 表ID：物理表组节点、表节点、字段节点、工人流节点对应的主表ID.* */
	private String tableId;
    /** 是否有子节点：0-否 1-是**/
    private Boolean child;
    /** 节点规则：0-常规节点， 1-触发器动态节点， 2-实时动态节点（生成树时直接从数据库中查询出的）.*/
    private String nodeRule = "0";
    /** 是否根据数据表来动态生成的节点：0-否，1-是.*/
    private String dynamic = "0";
    /** 是动态节点来源ID(记录当前节点的信息是从哪个动态节点来的).*/
    private String dynamicFromId;
    /** 备注.*/
    private String remark;
    /** 动态节点触发器用的：当前节点所有父节点的字段名称的拼接.表结点:"#T"+id,空节点："#E"+id,字段节点：columnName(字段英文名称)*/
    private String columnNames;
    /** 动态节点触发器用的：当前节点所有父节点的字段值的拼接.表结点:"#T"+id,空节点："#E"+id,字段节点：value*/
    private String columnValues;
    /** 动态节点触发器用的：当前节点所有父节点的id的拼接.*/
    private String parentIds;
    /** 生成动态节点数据来源：0-业务表，1-编码表.*/
    private String dataSource;
    /** 是否在节点上显示有多少条数据（0.否  1.是） **/
    private String showNodeCount;
    /** 动态节点排序方式：asc-升序 desc-降序*/
    private String sortType;
    /** 跟节点ID.*/
    private String rootId;
    /** 是否显示根节点：0-否，1-是*/
    private String showRoot;
    /** 物理表组节点和物理表节点的过滤条件*/
    private String columnFilter;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
    public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Column(name = "DB_ID")
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
        if (StringUtil.isEmpty(dbId)) {
        	dbId = "";
        }
		this.dbId = dbId;
	}
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public Boolean getChild() {
        return child;
    }
    public void setChild(Boolean child) {
        this.child = child;
    }
    public String getNodeRule() {
        return nodeRule;
    }
    public void setNodeRule(String nodeRule) {
        if (!"1".equals(nodeRule) && !"2".equals(nodeRule)) {
            nodeRule = "0";
        }
        this.nodeRule = nodeRule;
    }
    public String getDynamic() {
        return dynamic;
    }
    public void setDynamic(String dynamic) {
        if (!"1".equals(dynamic)) {
            dynamic = "0";
        }
        this.dynamic = dynamic;
    }
    public String getDynamicFromId() {
        return dynamicFromId;
    }
    public void setDynamicFromId(String dynamicFromId) {
        if (null != dynamicFromId && "null".equals(dynamicFromId.toLowerCase())) {
            dynamicFromId = "";
        }
        this.dynamicFromId = dynamicFromId;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        if ("1".equals(nodeRule)) {
            remark = "作为生成动态节点的信息记录，在实际展现中不出现 ！";
        }
        if (null != remark && "null".equals(remark.toLowerCase())) {
            remark = "";
        }
        this.remark = remark;
    }
	public String getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	public String getColumnValues() {
		return columnValues;
	}
	public void setColumnValues(String columnValues) {
		this.columnValues = columnValues;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
    	if (null != dataSource && "null".equals(dataSource.toLowerCase())) {
    		dataSource = "";
        }
        this.dataSource = dataSource;
    }
    public String getShowNodeCount() {
        return showNodeCount;
    }
    public void setShowNodeCount(String showNodeCount) {
        this.showNodeCount = showNodeCount;
    }
    public String getSortType() {
        return sortType;
    }
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
    public String getRootId() {
        return rootId;
    }
    public void setRootId(String rootId) {
        this.rootId = rootId;
    }
    public String getShowRoot() {
        return showRoot;
    }
    public void setShowRoot(String showRoot) {
        this.showRoot = showRoot;
    }
    public String getColumnFilter() {
		return columnFilter;
	}
	public void setColumnFilter(String columnFilter) {
		this.columnFilter = columnFilter;
	}
	@Override
    public Object clone() {
		TreeDefine cloneObj = null;
		try {
			cloneObj = (TreeDefine) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (cloneObj != null) {
			cloneObj.setId(null);
			cloneObj.setParentId(null);
		}
		return cloneObj;
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
        TreeDefine other = (TreeDefine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
