package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class TzjyszcdjyzhCode extends CodeApplication  {

	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeName(String value) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeTree(String codeTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCodeGrid(String codeTypeCode) {
		// TODO Auto-generated method stub
		Map<String, Object> gcfg = new HashMap<String, Object>();
		gcfg.put("colNames", getColNames());
		gcfg.put("colModel", getColModel());
		//gcfg.put("url", "tcspfs!getJyzdaGrid.json");
		gcfg.put("valueField", "SZCDJYZH");// 列表中PFSBM列值作为 显示值
		gcfg.put("textField", "SZCDJYZH");// 列表中PFSMC列值作为 隐藏值
		gcfg.put("panelWidth", 360);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("生猪产地检疫证号");
        list.add("货主编码");
        list.add("货主名称");
        list.add("ID");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SZCDJYZH");
		item.put("width", 180);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "HZBM");
		item.put("width", 90);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "HZMC");
		item.put("width", 90);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "ID");
		item.put("width", 0);
		item.put("hidden", true);
		colModel.add(item);
		
        return colModel;
    }
}
