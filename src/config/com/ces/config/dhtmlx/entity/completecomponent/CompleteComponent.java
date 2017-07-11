package com.ces.config.dhtmlx.entity.completecomponent;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 成品构件实体类
 * 
 * @author wanglei
 * @date 2014-02-17
 */
@Entity
@Table(name = "T_XTPZ_COMP_COMPONENT")
public class CompleteComponent extends StringIDEntity {

    private static final long serialVersionUID = 1790125662909662697L;

    /** * 成品构件编码 */
    private String code;

    /** * 成品构件名称 */
    private String name;

    /** * 成品构件显示名称 */
    private String alias;

    /** * 成品构件类别：1-页面构件 2-逻辑构件 3-自定义构件 4-组合构件 */
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
