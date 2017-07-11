package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 预留区绑定的构件和页面方法的绑定关系实体类
 * 
 * @author wanglei
 * @date 2013-08-27
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_FUNCTION")
public class ConstructFunction extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 组合构件中构件和预留区绑定关系ID */
    private String constructDetailId;

    /** * 页面方法ID */
    private String functionId;

    /** * 页面方法返回值ID */
    private String outputParamId;

    /** * 构件入参ID */
    private String inputParamId;

    public String getConstructDetailId() {
        return constructDetailId;
    }

    public void setConstructDetailId(String constructDetailId) {
        this.constructDetailId = constructDetailId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getOutputParamId() {
        return outputParamId;
    }

    public void setOutputParamId(String outputParamId) {
        this.outputParamId = outputParamId;
    }

    public String getInputParamId() {
        return inputParamId;
    }

    public void setInputParamId(String inputParamId) {
        this.inputParamId = inputParamId;
    }
}