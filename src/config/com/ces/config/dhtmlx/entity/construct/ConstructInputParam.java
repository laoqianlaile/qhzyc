package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 组合构件中基础构件的入参实体类
 * 
 * @author wanglei
 * @date 2013-09-03
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_INPUT_PARAM")
public class ConstructInputParam extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 组合构件绑定关系ID */
    private String constructId;

    /** * 构件入参ID */
    private String inputParamId;

    /** * 参数名称 */
    private String name;

    /** * 参数值 */
    private String value;

    public String getConstructId() {
        return constructId;
    }

    public void setConstructId(String constructId) {
        this.constructId = constructId;
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