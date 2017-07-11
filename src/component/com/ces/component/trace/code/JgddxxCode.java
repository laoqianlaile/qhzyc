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
public class JgddxxCode extends CodeApplication{
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
//		gcfg.put("url", "jgddxx!getDdxxGrid.json");
		gcfg.put("valueField", "DDBH");// 列表中DDBH列值作为 显示值
		gcfg.put("textField", "DDBH");// 列表中DDBH列值作为 隐藏值
        gcfg.put("panelWidth", 400);
		return gcfg;
	}
	
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("订单编号");
        list.add("成品名称");
        list.add("客户名称");
        list.add("下单时间");
        
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "DDBH");
		item.put("width", 100);
		colModel.add(item);
		
		
		item = new HashMap<String, Object>();
		item.put("name", "CPMC");
		item.put("width", 100);
		colModel.add(item);
	
		
		item = new HashMap<String, Object>();
		item.put("name", "KHMC");
		item.put("width", 100);
		colModel.add(item);
		
		
		item = new HashMap<String, Object>();
		item.put("name", "XDSJ");
		item.put("width", 100);
		colModel.add(item);
		
        return colModel;
    }
}
