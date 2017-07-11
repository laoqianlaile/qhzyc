package com.ces.config.dhtmlx.json.entity.common;

import com.ces.xarch.core.web.jackson.JacksonBean;

public class DhtmlxTreeNode extends JacksonBean {

    private String id;

    private String text;

    private String type;

    private String child;

    private String checked;

    private String im0;

    private String im1;

    private String im2;

    private String prop0;

    private String prop1;

    private String prop2;

    private String prop3;

    private String prop4;

    private boolean open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getIm0() {
        return im0;
    }

    public void setIm0(String im0) {
        this.im0 = im0;
    }

    public String getIm1() {
        return im1;
    }

    public void setIm1(String im1) {
        this.im1 = im1;
    }

    public String getIm2() {
        return im2;
    }

    public void setIm2(String im2) {
        this.im2 = im2;
    }

    public String getProp0() {
        return prop0;
    }

    public void setProp0(String prop0) {
        this.prop0 = prop0;
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3;
    }

    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
