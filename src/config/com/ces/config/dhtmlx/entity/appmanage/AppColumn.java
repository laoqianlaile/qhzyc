package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
@Entity
@Table(name = "T_XTPZ_APP_COLUMN")
public class AppColumn extends StringIDEntity {
    
    private static final long serialVersionUID = -6783635615990553819L;
    // 操作列字段别名前缀
    private static final String COLUMN_ALIAS_PRE = "COL_";
    // 特殊值：如字段ID
    public static final String SPECIAL_VALUE = "-1";
    
    private String tableId;
    
    private String componentVersionId;
    
    private String menuId;
    
    private String userId;// 用户ID
    
    private String columnId;// 字段ID: -1为手动添加列
    
    private String columnName;
    
    private String columnType;// 字段类型：0-表字段，1-自定义SQL语句，2-固定值，3-超链接预留区
    
    private String columnAlias;
    
    private String showName;
    
    private String align;
    
    private Integer width; // -1为自适应
    
    private Integer showOrder;
    /* 列表列类型：
     * ro-文本只读 link-链接  
     * ro_card-文本和缩略图信息  
     * card-缩略图信息  
     * value-显示编码（如果是编码类型，则显示编码value，不进行转换）  
     * hidden-隐藏列
     * editable-可编辑
     */
    private String type;  
    
    private String url;   // 链接地址

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

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnAlias() {
    	if (StringUtil.isNotEmpty(columnAlias)) return columnAlias;
        if (!"0".equals(getColumnType()) || SPECIAL_VALUE.equals(getColumnId())) {
            return COLUMN_ALIAS_PRE + StringUtil.fillZero(3, showOrder);
        }
        return getColumnName();
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    /***********************(辅助属性,不保存)************************/
    private String dataType;    
    private String codeTypeCode;
    private String inputType;
    private String dataTypeExtend;
    private Integer length;
    @Transient
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Transient
    public String getCodeTypeCode() {
        return codeTypeCode;
    }

    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }
    @Transient
    public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
    @Transient
	public String getDataTypeExtend() {
		return dataTypeExtend;
	}

	public void setDataTypeExtend(String dataTypeExtend) {
		this.dataTypeExtend = dataTypeExtend;
	}
    @Transient
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public static interface Type {
        String TEXT = "ro";    // 文本
        String HIDDEN = "hidden"; // 隐藏列
        String LINK = "link";  // 链接
        String CARD = "card";  // 缩略图信息
        String TEXT_CARD = "ro_card";  // 文本和缩略图信息都显示
        //String IMG  = "img";   // 图片
        String VALUE = "value"; // 显示编码
        String EDITABLE = "editable"; // 可编辑
    }
}
