package com.ces.config.datamodel.option;

/**
 * 
 * <p>描述: 下拉框、单复选框</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-3 下午7:16:16
 *
 */
public class OptionModel {
    // 隐藏值
    private String value;
    // 显示值
    private String text;
    // 是否选中
    private Boolean selected = Boolean.FALSE;
    // 样式
    private String css;
    // logo图标
    private String img;
    
    private Object prop;
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    public String getCss() {
        return css;
    }
    public void setCss(String css) {
        this.css = css;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
	public Object getProp() {
		return prop;
	}
	public void setProp(Object prop) {
		this.prop = prop;
	}
}
