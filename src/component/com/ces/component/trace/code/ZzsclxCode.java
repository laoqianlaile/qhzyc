package com.ces.component.trace.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
/**
 * 上海中信信息发展股份有限公司
 * 
 * @author Hpu.Plain
 * 
 * @version V1.0
 * 
 * @category 复合框下拉列表数据：蔬菜种植-蔬菜类型（菜种档案）
 * 
 */
public class ZzsclxCode extends CodeApplication{

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
		gcfg.put("url", "tcszzspxx!getSpxxGridData.json");
		//设置点击是，需要获得的是那个数据  textField 显示的数据，和隐藏数据
		gcfg.put("valueField", "SPBM");
		gcfg.put("textField", "SPMC");
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("商品编号");
        list.add("商品名称");
        return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;
		
		item = new HashMap<String, Object>();
		item.put("name", "SPBM");
		item.put("width", 125);
		colModel.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "SPMC");
		item.put("width", 125);
		colModel.add(item);
		
        return colModel;
    }

}
