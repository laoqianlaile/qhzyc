package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class LsJcbhCode extends CodeApplication{

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
		//gcfg.put("url", "lsscjcxx!getJcbh.json");
		gcfg.put("valueField", "ID");
		gcfg.put("textField", "SCJCBH");
		gcfg.put("panelWidth", 500);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("id");
        list.add("进场编号");
        list.add("批发商编码");
        list.add("批发商名称");
        list.add("进场日期");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "ID");
		item.put("hidden", true);
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "SCJCBH");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PFSBM");
		item.put("width", 150);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "PFSMC");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "JCRQ");
		item.put("width", 100);
		colModel.add(item);
		
        return colModel;
    }

}
