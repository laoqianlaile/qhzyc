package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZzqyxxCode extends CodeApplication{

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
		gcfg.put("url", "tcssczzqyxx!getQyxxGridData.json");
		gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
		gcfg.put("valueField", "QYBH");
		gcfg.put("textField", "QYMC");
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("区域编码");
        list.add("区域名称");
        list.add("面积");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "QYBH");
		item.put("width", 60);
		item.put("align", "left");
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "QYMC");
		item.put("width", 100);
		item.put("align", "left");
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "QYMJ");
		item.put("width", 40);
		item.put("align", "right");
		colModel.add(item);


		return colModel;
    }
}
