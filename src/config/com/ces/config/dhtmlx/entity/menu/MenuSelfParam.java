package com.ces.config.dhtmlx.entity.menu;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 菜单绑定构件的自身配置参数实体类
 * 
 * @author wanglei
 * @date 2013-09-11
 */
@Entity
@Table(name = "T_XTPZ_MENU_SELF_PARAM")
public class MenuSelfParam extends StringIDEntity {

    private static final long serialVersionUID = 5936531152661107505L;

    /** * 构件ID 或 组合构件中基础构件版本ID */
    private String componentVersionId;

    /** * 菜单ID */
    private String menuId;

    /** * 构件自身参数ID */
    private String selfParamId;

    /** * 参数名称 */
    private String name;

    /** * 参数类型 0-文本框 1-普通下拉框 2-Table 3-Tabel_Column 4-Code */
    private String type;

    /** * 参数默认值 */
    private String value;

    /** * 参数说明 */
    private String remark;

    /** * 下拉框选项 */
    private String options;

    /** * 参数列表上显示的值 */
    private String text;

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

    public String getSelfParamId() {
        return selfParamId;
    }

    public void setSelfParamId(String selfParamId) {
        this.selfParamId = selfParamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
