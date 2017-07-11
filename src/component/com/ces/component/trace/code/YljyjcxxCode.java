package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YljyjcxxCode extends CodeApplication{

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
		gcfg.put("url", "sdzycyljyjcxx!getZzpchGrid.json");
		gcfg.put("valueField", "QYPCH");
		gcfg.put("textField", "QYPCH");
        gcfg.put("gridOptions",getGridOptions());
		gcfg.put("panelWidth", 350);
		return gcfg;
	}

    protected Map<String, Object> getGridOptions () {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rowNum",10);
        return map;
    }

	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
		list.add("种植批次号");
		list.add("种植批次号");
		list.add("种子种苗名称");
		list.add("药材代码");
		list.add("药材名称");
		list.add("种植基地");
		list.add("种植地块");
		list.add("种植面积");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;

		item = new HashMap<String, Object>();
		item.put("name", "QYPCH");
		item.put("width", 200);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "ZZPCH");
		item.put("hidden", true);
		item.put("width", 200);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "ZMZZMC");
		item.put("hidden", true);
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YCDM");
		item.put("hidden", true);
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "YCMC");
		item.put("width", 100);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JDMC");
		item.put("hidden", true);
		item.put("width", 150);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "DKMC");
		item.put("hidden", true);
		item.put("width", 150);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "DKMJ");
		item.put("width", 150);
		colModel.add(item);

        return colModel;
    }
}
