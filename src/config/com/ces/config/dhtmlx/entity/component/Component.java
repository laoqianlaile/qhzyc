package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件实体类
 * 
 * @author wanglei
 * @date 2013-07-22
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT")
public class Component extends StringIDEntity {

    private static final long serialVersionUID = -5644387974413332033L;

    /** * 构件编码 */
    private String code;

    /** * 构件名称 */
    private String name;

    /** * 构件显示名称 */
    private String alias;

    /** * 构件类别： 0-公用构件 1-页面构件 2-逻辑构件 3-树构件 4-物理表构件 5-逻辑表构件 6-通用表构件 9-组合构件*/
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
