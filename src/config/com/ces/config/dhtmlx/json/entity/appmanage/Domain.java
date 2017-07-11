package com.ces.config.dhtmlx.json.entity.appmanage;

import com.ces.xarch.core.web.jackson.JacksonBean;

public class Domain extends JacksonBean{
    /** 宽度.*/
    private Integer width = new Integer(0);
    /** 高度.*/
    private Integer height= new Integer(0);

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
