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
@Table(name = "T_XTPZ_APP_FORM_ELEMENT")
public class AppFormElement extends StringIDEntity implements Cloneable {

    private static final long serialVersionUID = 7799709677991573266L;
    /** 分栏符columnId值 */
    public static final String SUBFIELD_ID = "-1";
    /** 点位符columnId值 */
    public static final String PLACEHOLDER_ID = "-2";
    /** 表ID */
    private String tableId;
    /** 模块ID */
    private String componentVersionId;
    /** 菜单ID */
    private String menuId;
    /** 显示顺序 */
    private Integer showOrder;
    /** 字段ID (当值为-1时，表示分栏符)*/
    private String columnId; 
    /** 字段显示名称.**/
    private String showName;
    /** 占用列数 */
    private short colspan = 1;
    /** 必输：0-否 1-是 */
    private String required;
    /** 只读：0-否 1-是 */
    private String readonly;
    /** 隐藏：0-否 1-是 */
    private String hidden;
    /** 文本域：0-否 1-是 */
    //private String textarea;
    /** 默认值 */
    private String defaultValue;
    /** 连续录入：0-否 1-是 */
    private String kept;
    /** 递增：0-否 1-是 */
    private String increase;
    /** 继承：0-否 1-是 */
    private String inherit;
    /** 校验 */
    private String validation;
    /** 提示信息 */
    private String tooltip;
    /** 正则表达式 */
    private String pattern;
    /** 占列数（colspan）的百分比 */
    private short spacePercent = 100;
    
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
    public Integer getShowOrder() {
        return showOrder;
    }
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
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
    public short getColspan() {
        return colspan;
    }
    public void setColspan(short colspan) {
        this.colspan = colspan;
    }
    public String getRequired() {
        return required;
    }
    public void setRequired(String required) {
        this.required = required;
    }
    public String getReadonly() {
        return readonly;
    }
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }
    public String getHidden() {
        return hidden;
    }
    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
    /*public String getTextarea() {
        return textarea;
    }
    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }*/
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public String getKept() {
        return kept;
    }
    public void setKept(String kept) {
        this.kept = kept;
    }
    public String getIncrease() {
        return increase;
    }
    public void setIncrease(String increase) {
        this.increase = increase;
    }
    public String getInherit() {
        return inherit;
    }
    public void setInherit(String inherit) {
        this.inherit = inherit;
    }
    public String getValidation() {
        return validation;
    }
    public void setValidation(String validation) {
        this.validation = validation;
    }
    public String getTooltip() {
        return tooltip;
    }
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public short getSpacePercent() {
		return spacePercent;
	}
	public void setSpacePercent(short spacePercent) {
		this.spacePercent = spacePercent;
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
    
    /***********************以下属性不作为表字段用*************************/
    /** 是否为表单字段.*/
    private String formable;
    /** 字段英文名称.**/
    private String columnName;
    /** 字段类型.**/
    private String dataType;
    /** 字段类型扩展.**/
    private String dataTypeExtend;
    /** 字段长度.**/
    private String length;
    /** 编码ID.**/
    private String codeTypeCode;
    /** 置灰标记.**/
    private String disabled;
    /** 输入框类型.**/
    private String inputType;
    /** 输入框属性.**/
    private String inputOption;
    @Transient
    public String getFormable() {
        return formable;
    }
    public void setFormable(String formable) {
        this.formable = formable;
    }
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
    public String getLength() {
        return length;
    }
    
    public void setLength(String length) {
        this.length = length;
    }
    @Transient
    public String getCodeTypeCode() {
        return codeTypeCode;
    }
    
    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }
    @Transient
    public String getDisabled() {
        return disabled;
    }
    
    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
    @Transient
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	@Transient
	public String getInputOption() {
		return inputOption;
	}
	public void setInputOption(String inputOption) {
		this.inputOption = inputOption;
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
