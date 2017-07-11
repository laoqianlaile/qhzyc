package com.ces.config.dhtmlx.json.entity.appmanage;

import com.ces.xarch.core.web.jackson.JacksonBean;

public class DhtmlxToobarItem extends JacksonBean {
    
    /* 按钮*/
    public static final String T_BUTTON    = "b";
    /* 分隔线*/
    public static final String T_SEPARATOR = "s";

    private String id;
    private Integer pos;
    private String name;
    private String image;
    private String type;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getPos() {
        return pos;
    }
    public void setPos(Integer pos) {
        this.pos = pos;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
