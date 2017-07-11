package com.ces.config.utils;

public class FormUtil {
    
    public final static String FT_INPUT    = "input";
    public final static String FT_TEXTAREA = "textarea";
    public final static String FT_HIDDEN   = "hidden";
    public final static String FT_CALENDAR = "calendar";
    public final static String FT_COMBO    = "combo";
    public final static String FT_NUMBER   = "number";
    public final static String FT_BUTTON   = "button";
    public final static int WIDTH_LABEL = 100;
    public final static int WIDTH_INPUT = 200;
    
    /**
     * qiucs 2015-1-22 
     * <p>描述: 组件库4.0表单元素类型</p>
     */
    public static interface CoralInputType {
    	// 隐藏
    	String HIDDEN = "hidden";
    	// 文本框
    	String TEXTBOX   = "textbox";
    	// 文本框
//    	String TEXTBOXBUTTON   = "textboxbutton";
    	// 日期框
    	String DATEPICKER = "datepicker";
    	// 文本域
    	String TEXTAREA = "textarea";
    	// 下拉框
    	String COMBOBOX = "combobox";
    	// 下拉列表
    	String COMBOGRID = "combogrid";
    	// 复选框
    	String CHECKBOX = "checkbox";
    	// 单选框
    	String RADIO = "radio";
    	// 下拉树
    	String COMBOTREE = "combotree";
    }
    
    /**
     * qiujinwei 2015-07-07 
     * <p>描述: 组件库4.0表单元素属性</p>
     */
    public static interface CoralInputOption {
    	//文本框
    	String TEXTBOX = "textbox";
    	//文本框 + 按钮
    	String TEXTBOXBUTTON = "textboxbutton";
    	//文本框 + 标签
    	String TEXTBOXLABEL = "textboxlabel";
    	//整型
    	String INTEGER = "integer";
    	//浮点型
    	String FLOATING = "floating";
    	//整型 + 标签
    	String INTEGERLABEL = "integerlabel";
    	//浮点型 + 标签
    	String FLOATINGLABEL = "floatinglabel";
    	//弹出树
    	String POPUP = "popup";
    	//下拉树
    	String PULLDOWN = "pulldown";
    	//下拉列表
    	String COMBOGRID = "combogrid";
    	//下拉列表 + 按钮
    	String COMBOGRIDBUTTON = "combogridbutton";
    }
    
    /**
     * <p>标题: getItemType</p>
     * <p>描述: DHTMLX表单元素类型</p>
     * @param  hidden
     * @param  type
     * @param  textarea
     * @return String    返回类型   
     * @throws
     */
    public static String getItemType(boolean hidden, String inputType) {
        if (hidden) {
            return FT_HIDDEN;
        }
        if (FT_BUTTON.equals(inputType)) {
            return FT_BUTTON;
        }
        
        /*if (ConstantVar.DataType.CHAR.equals(type)) {
            if (textarea) return FT_TEXTAREA;
            return FT_INPUT;
        }
        if (ConstantVar.DataType.NUMBER.equals(type)) {
            return FT_NUMBER;
        } else if (ConstantVar.DataType.DATE.equals(type)) {
            return FT_CALENDAR;
        }*/
        
        return FT_COMBO;
    }

    /**
     * qiucs 2013-9-11 
     * <p>描述: 创建新的BLOCK</p>
     * @param  colspan
     * @return StringBuffer    返回类型   
     */
    public static StringBuffer newFormBlock(Integer colspan, String nameSuffix) {
        return new StringBuffer("{type: \"block\", width: " + getWidth(colspan) + ", name: \"block_" + nameSuffix + "\", list:[");
    }

    /**
     * qiucs 2014-3-6 
     * <p>描述: 创建新的BLOCK</p>
     * @param  width
     * @return StringBuffer    返回类型   
     */
    public static StringBuffer newFormBlock(int width, String nameSuffix) {
        return new StringBuffer("{type: \"block\", width: " + width + ", name: \"block_" + nameSuffix + "\", list:[");
    }
    
    /** 
     * qiucs 2013-12-27 
     * <p>描述: 表单宽度</p>
     * @param  colspan
     * @return int    返回类型   
     */
    public static int getWidth(Integer colspan) {
        return ((FormUtil.WIDTH_LABEL + FormUtil.WIDTH_INPUT + 20) * colspan);
    }
    
    /**
     * qiucs 2015-4-3 上午9:17:20
     * <p>描述: 默认值处理 </p>
     * @param format 
     *          -- 1. name: 获取显示名称；
     *          -- 2. code: 获取显示编码；
     *          -- 3. 日期格式：如  yyyy-MM-dd
     * @return String
     */
    public static String processDefaultValue(String defaultValue, String format) {
    	
    	if (ConstantVar.CurrentValue.USER.equals(defaultValue)) {
    		defaultValue = "name".equals(format) ? CommonUtil.getUser().getName() : CommonUtil.getCurrentUserId();
    	} else if (ConstantVar.CurrentValue.DEPT.equals(defaultValue)) {
    		defaultValue = CommonUtil.getCurrentDeptId();
    	} else if (ConstantVar.CurrentValue.DATE.equals(defaultValue)) {
        	defaultValue = DateUtil.now(format);
    	}
    	
    	return defaultValue;
    }
}
