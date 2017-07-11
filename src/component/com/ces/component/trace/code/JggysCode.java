package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

public class JggysCode extends CodeApplication  {

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
		Map<String, Object> gys = new HashMap<String, Object>();
		gys.put("colNames", getColNames());
		gys.put("colModel", getColModel());
		//gys.put("url", "tcspfs!getJyzdaGrid.json");
		gys.put("valueField", "GYSBM");// 列表中PFSBM列值作为 显示值
		gys.put("textField", "GYSMC");// 列表中PFSMC列值作为 隐藏值
		gys.put("panelWidth", 350);
		return gys;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("供应商编码");
        list.add("供应商名称");
        list.add("供应商证件号");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "GYSBM");
		item.put("width", 100);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "GYSMC");
		item.put("width", 125);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "GYSZJH");
		item.put("width", 125);
		colModel.add(item);

        return colModel;
    }

}
