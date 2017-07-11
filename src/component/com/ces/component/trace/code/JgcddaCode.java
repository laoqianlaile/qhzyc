package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

/**
 * @author Hpu.Plain
 *
 */
public class JgcddaCode extends CodeApplication{
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
		//gcfg.put("url", "jgcdda!getCddaGrid.json");
		gcfg.put("valueField", "CDBH");// 列表中CDBH列值作为 显示值
		gcfg.put("textField", "CDMC");// 列表中CDMC列值作为 隐藏值
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("场地编号");
        list.add("场地名称");
       //list.add("岗位");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "CDBH");
		item.put("width", 125);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "CDMC");
		item.put("width", 125);
		colModel.add(item);
		
        return colModel;
    }
}
