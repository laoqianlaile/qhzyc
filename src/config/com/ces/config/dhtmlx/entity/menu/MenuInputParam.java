package com.ces.config.dhtmlx.entity.menu;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 菜单绑定的构件入参实体类
 * 
 * @author wanglei
 * @date 2014-09-11
 */
@Entity
@Table(name = "T_XTPZ_MENU_INPUT_PARAM")
public class MenuInputParam extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 菜单ID */
    private String menuId;

    /** * 构件入参ID */
    private String inputParamId;

    /** * 参数名称 */
    private String name;

    /** * 参数值 */
    private String value;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getInputParamId() {
        return inputParamId;
    }

    public void setInputParamId(String inputParamId) {
        this.inputParamId = inputParamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}