package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CspchCode extends CodeApplication{

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
		gcfg.put("url", "sdzyccscggl!getCspchGrid.json");
		gcfg.put("valueField", "QYPCH");
		gcfg.put("textField", "QYPCH");
        gcfg.put("gridOptions",getGridOptions());
		gcfg.put("panelWidth", 420);
//		gcfg.put("pager",false);
		return gcfg;
	}

    protected Map<String, Object> getGridOptions () {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rowNum",20);
        return map;
    }

	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
		list.add("采收批次号");
        list.add("采收批次号");
		list.add("药材代码");
		list.add("药材名称");
		list.add("采收重量");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;

		item = new HashMap<String, Object>();
		item.put("name", "QYPCH");
		item.put("width", 140);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "CSPCH");
		item.put("hidden",true);
		item.put("width", 260);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YCDM");
		item.put("hidden", true);
		item.put("width", 110);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YCMC");
		item.put("width", 140);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "CSZL");
		item.put("hidden",true);
		item.put("width", 140);
		colModel.add(item);

        return colModel;
    }
}
