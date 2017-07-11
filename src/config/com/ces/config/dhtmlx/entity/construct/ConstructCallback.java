package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 预留区绑定的构件和回调函数的绑定关系实体类
 * 
 * @author wanglei
 * @date 2013-09-28
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_CALLBACK")
public class ConstructCallback extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 组合构件中构件和预留区绑定关系ID */
    private String constructDetailId;

    /** * 回调函数ID(供构件关闭时使用) */
    private String callbackId;

    /** * 构件出参ID */
    private String outputParamId;

    /** * 回调函数的入参ID */
    private String inputParamId;

    public String getConstructDetailId() {
        return constructDetailId;
    }

    public void setConstructDetailId(String constructDetailId) {
        this.constructDetailId = constructDetailId;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
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