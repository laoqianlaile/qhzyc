package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScspxxCode extends CodeApplication{

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
		gcfg.put("url", "scscspxx!getspxxGrid.json");
		gcfg.put("gridOptions", "{ajaxGridOptions:{async:false}}");
		gcfg.put("valueField", "SPBH");
		gcfg.put("textField", "SPBH");
		gcfg.put("panelWidth", 250);
        gcfg.put("multiple", true);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商铺编号");
        list.add("位置");
        list.add("面积");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SPBH");
		item.put("width", 80);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "WZ");
		item.put("width", 80);
		colModel.add(item);

        item = new HashMap<String, Object>();
		item.put("name", "MJ");
		item.put("width", 100);
		colModel.add(item);

        return colModel;
    }
}
