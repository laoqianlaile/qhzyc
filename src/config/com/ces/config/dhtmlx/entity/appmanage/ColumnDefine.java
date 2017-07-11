package com.ces.config.dhtmlx.entity.appmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 字段定义实体类
 * @author wangmi
 * @date 2013-06-21 12:56:03
 */

@Entity
@Table(name = "T_XTPZ_COLUMN_DEFINE")
public class ColumnDefine extends  StringIDEntity{
	private static final long serialVersionUID = 1156757913627217913L;
	
	/** 关联表ID * */
	private String tableId;
	
	/** 显示名称 * */
	private String showName;
	
	/** 字段名称 * */
	private String columnName;
	
	/** 数据类型 : c-字符类型, n-数字类型 , d-日期类型* */
	private String dataType;
	
	/** 数据类型扩展:d-日期格式显示 t-时间格式显示 m-年月格式显示 数字-浮点格式长度 标签{lable: }* */
	private String dataTypeExtend;
	
	/** 编码表ID * */
	private String codeTypeCode;
	
	/**  长度 * */
	private Integer length;
	
	/** 类型：0-业务字段 1-基础字段 2-系统字段* */
	private String columnType="0";
	
	/** 是否录入项：0-否 1-是 * */
	private String inputable;
	
	/** 是否修改项：0-否 1-是 * */
	private String updateable;
	
	/** 是否检索项：0-否 1-是 * */
	private String searchable;
	
	/** 是否列表项：0-否 1-是 * */
	private String listable;
	
	/** 是否排序项：0-否 1-是 * */
	private String sortable;

	/** 是否一体化检索项：0-否 1-是 * */
	private String phraseable;
	
	/** 对齐方式：left-靠左 center-居中 right-靠右 * */
	private String align;
	
	/** 查询方式：GT:大于;EQ: 等于;LT:小于;LIKE:包含;NOT:不等于；GTE:大于等于；LTE:小于等于;BT:范围；NLL：为空  * */
	private String filterType;
	
	/** 列表宽度 * */
	private Integer width;
	
	/**  默认值 * */
	private String defaultValue;
	
	/** 备注    * */
	private String remark;
	
	/** 是否生成物理字段：0-否 1-是 * */
	private String created;
	
	/** 字段标签(ColumnLabel.code)* */
	private String columnLabel;
	
	/** 显示顺序* */
	private Integer showOrder;
	
	/** 输入框类型：
	 * textbox-文本框; datepicker-日期框; textarea-文本域; 
	 * checkbox-复选框组; radio-单选框组; 
	 * combobox-下拉框; combotree-树; 
	 * combogrid-下拉列表
	 **/
	private String inputType;
	
	/** 输入框类型扩展属性 */
	private String inputOption;
	
	/********************* (辅助属性，不存在数据表中) ************************/
	/** 标签 */
	private String label;
	
	/** 精度 */
	private String precision;

    //公共字典表ID
    public static String COMMON_TABLE_ID = "-C";

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName.toUpperCase();
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataTypeExtend() {
		return dataTypeExtend;
	}

	public void setDataTypeExtend(String dataTypeExtend) {
		this.dataTypeExtend = dataTypeExtend;
	}

	public String getCodeTypeCode() {
		return codeTypeCode;
	}

	public void setCodeTypeCode(String codeTypeCode) {
		this.codeTypeCode = codeTypeCode;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
	    if (null != length) {
	        if (ConstantVar.DataType.NUMBER.equals(getDataType()) && length.intValue() > 38) {
	            length = 38;
	        } else if (ConstantVar.DataType.CHAR.equals(getDataType()) && length.intValue() > 4000) {
	            length = 4000;
	        }
	    }
		this.length = length;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getInputable() {
		return inputable;
	}

	public void setInputable(String inputable) {
		this.inputable = inputable;
	}

	public String getUpdateable() {
		return updateable;
	}

	public void setUpdateable(String updateable) {
		this.updateable = updateable;
	}

	public String getSearchable() {
		return searchable;
	}

	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}

	public String getListable() {
		return listable;
	}

	public void setListable(String listable) {
		this.listable = listable;
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public String getPhraseable() {
		return phraseable;
	}

	public void setPhraseable(String phraseable) {
		this.phraseable = phraseable;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInputOption() {
		return inputOption;
	}

	public void setInputOption(String inputOption) {
		this.inputOption = inputOption;
	}
	
	private JsonNode node = null;

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
	
	/**
     * 把扩展属性转成JsonNode
     * 
     * @return JsonNode
     */
    public JsonNode dataTypeExtend2node() {
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
    
    public void uploadDataTypeExtend (){
    	if (StringUtil.isNotEmpty(getInputOption()) &&  getInputOption().equals(FormUtil.CoralInputOption.FLOATINGLABEL)) {
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("label", getLabel());
    		map.put("precision", getPrecision());
    		setDataTypeExtend(JsonUtil.bean2json(map));
		}
    }
    
    /**
     * qiucs 2015-8-28 上午10:43:55
     * <p>描述: 字段对应的编码 </p>
     * @return List<Code>
     */
    @Transient
    public List<Code> getCodes() {
    	if (StringUtil.isNotEmpty(codeTypeCode)) {
    		return CodeUtil.getInstance().getCodeList(codeTypeCode);
    	}
    	return null;
    }
	
}
