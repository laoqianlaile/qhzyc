package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class JgylbhCode extends CodeApplication{

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		// TODO Auto-generated method stub
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		//gcfg.put("url", "jggwxx!getGwxxGridData.json");
		gcfg.put("valueField", "YLBH");
		gcfg.put("textField", "YLMC");
		gcfg.put("panelWidth", 300);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("原料编号");
        list.add("原料名称");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "YLBH");
		item.put("width", 170);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "YLMC");
		item.put("width", 170);
		colModel.add(item);

        return colModel;
    }
}
