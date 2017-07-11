package com.ces.component.trace.code;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.List;

/**
 * Created by wngyu on 15/11/27.
 */
public class YjztCode extends CodeApplication {

    @Override
    public String getCodeValue(String name) {
        return null;
    }

    @Override
    public String getCodeName(String value) {
        return null;
    }

    @Override
    public List<Code> getCodeList(String codeTypeCode) {
        return DataDictionaryUtil.getInstance().getDictionaryData("ZZYJZT");
    }

    @Override
    public Object getCodeTree(String codeTypeCode) {
        return null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
        return null;
    }
}
