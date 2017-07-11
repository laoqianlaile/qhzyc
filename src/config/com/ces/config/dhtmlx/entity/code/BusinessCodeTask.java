package com.ces.config.dhtmlx.entity.code;

import java.util.TimerTask;

import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.xarch.core.web.listener.XarchListener;

public class BusinessCodeTask extends TimerTask {

    private String codeTypeCode;

    public BusinessCodeTask(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }

    @Override
    public void run() {
        XarchListener.getBean(BusinessCodeService.class).syncBusinessCode(codeTypeCode);
    }

    public String getCodeTypeCode() {
        return codeTypeCode;
    }

    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }

}
