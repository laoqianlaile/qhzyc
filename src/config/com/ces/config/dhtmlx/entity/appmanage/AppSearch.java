package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.utils.FormUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "T_XTPZ_APP_SEARCH")
public class AppSearch extends StringIDEntity implements Cloneable {

    private static final long serialVersionUID = 848075446354452119L;
    
    private String tableId;
    
    private String componentVersionId;
    //默认为-1，表示按构件配置，否则为按菜单配置
    private String menuId;
    
    private String userId;
    
    private String columnId;
    
	private String showName;
    
    private Integer showOrder;
    
    private String filterType;

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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }
    
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
    
    // 辅助属性不保存数据库
	private String columnName;
	private String dataType;
	private String dataTypeExtend;
	private String inputType;
	private String codeTypeCode;
	private String inputOption;
	@Transient
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    @Transient
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Transient
    public String getDataTypeExtend() {
		return dataTypeExtend;
	}

	public void setDataTypeExtend(String dataTypeExtend) {
		this.dataTypeExtend = dataTypeExtend;
	}

	@Transient
    public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	@Transient
    public String getCodeTypeCode() {
        return codeTypeCode;
    }

    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }
    
    @Transient
    public String getInputOption() {
		return inputOption;
	}

	public void setInputOption(String inputOption) {
		this.inputOption = inputOption;
	}

	@Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/***********************以下属性为dataTypeExtend中保存的属性*************************/
	/** 标签 */
	private String label;
	
	/** 精度 */
	private String precision;
	
	@Transient
	public String getLabel() {
		if (StringUtil.isNotEmpty(label)) {
			return label;
		} else if (StringUtil.isNotEmpty(getInputOption()) && getInputOption().equals(FormUtil.CoralInputOption.FLOATINGLABEL)) {
			return obtainJsonValue("label");
		}
		return "";
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Transient
	public String getPrecision() {
		if (StringUtil.isNotEmpty(precision)) {
			return precision;
		} else if (StringUtil.isNotEmpty(getInputOption()) && getInputOption().equals(FormUtil.CoralInputOption.FLOATINGLABEL)) {
			return obtainJsonValue("precision");
		}
		return "";
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}
	
	private JsonNode node = null;
	
	/**
     * 把扩展属性转成JsonNode
     * 
     * @return JsonNode
     */
    private JsonNode dataTypeExtend2node() {
        String str = getDataTypeExtend();
        if (StringUtil.isEmpty(str))
            return null;
        return JsonUtil.json2node(str);
    }
    
    /**
     * 获取JsonNode中对应属性值
     * 
     * @return String
     */
    private String obtainJsonValue(String prop) {
        if (null == node)
            node = dataTypeExtend2node();
        if (null != node ) {
            JsonNode jsonNode = node.get(prop);
            if (jsonNode == null)
                return "";
            return jsonNode.asText();
        }
        return "";
    }

}
