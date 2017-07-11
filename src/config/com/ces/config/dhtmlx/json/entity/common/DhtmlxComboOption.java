package com.ces.config.dhtmlx.json.entity.common;

import com.ces.xarch.core.web.jackson.JacksonBean;

public class DhtmlxComboOption extends JacksonBean {
    // 隐藏值
    private String value;
    // 显示值
    private String text;
    // 是否选中
    private Boolean selected = Boolean.FALSE;
    // 样式
    private String css;
    // logo图标
    private String img_src;
    
    private String prop1;

    public DhtmlxComboOption() {
        super();
    }

    public DhtmlxComboOption(String value, String text) {
        this.value = value;
        this.text = text;
    }

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

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

	/**
	 * <p>获取属性prop1.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-18  18:16:49
	 */
	public String getProp1() {
		return prop1;
	}

	/**
	 * 设置属性prop1.
	 * @param prop1 
	 * @author Administrator
	 * @date 2013-11-18  18:16:49
	 */
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

}
