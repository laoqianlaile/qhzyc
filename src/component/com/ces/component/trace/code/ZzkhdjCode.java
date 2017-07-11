package com.ces.component.trace.code;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.List;
import java.util.Map;

public class ZzkhdjCode extends CodeApplication{

	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeName(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		return DataDictionaryUtil.getInstance().getDictionaryData("KHLX");
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		return null;
	}
	
	protected List<String> getColNames() {
        return null;
    }
	
	protected List<Map<String, Object>> getColModel() {
        return null;
    }
}
