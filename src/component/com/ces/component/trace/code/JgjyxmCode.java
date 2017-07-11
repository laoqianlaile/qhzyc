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
 * 
 * 	包装材料种类复合框下拉列表数据
 *
 */
public class JgjyxmCode extends CodeApplication{
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
		gcfg.put("url", "jgjgjyxm!getBm.json");
		gcfg.put("valueField", "JYXMBH");// 列表中隐藏值
		gcfg.put("textField", "JYXMMC");// 列表中显示值
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("检验项目编号");
        list.add("检验项目名称");
        
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "JYXMBH");
		item.put("width", 125);
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "JYXMMC");
		item.put("width", 125);
		colModel.add(item);
		

		
        return colModel;
    }
}
