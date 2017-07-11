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
public class JgtjjzlCode extends CodeApplication{
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
//		gcfg.put("url", "jgtjjzl!getTjjzlGrid.json");
		gcfg.put("valueField", "TJJBH");// 列表中CDBH列值作为 显示值
		gcfg.put("textField", "TJJTYM");// 列表中CDMC列值作为 隐藏值
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("添加剂编号");
        list.add("添加剂名称");
       //list.add("岗位");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "TJJBH");
		item.put("width", 125);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "TJJTYM");
		item.put("width", 125);
		colModel.add(item);
		
        return colModel;
    }
}
