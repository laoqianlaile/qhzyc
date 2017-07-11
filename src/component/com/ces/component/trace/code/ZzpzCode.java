package com.ces.component.trace.code;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZzpzCode extends CodeApplication{

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
//		//1.数据权限过滤   2.是否排序  3.状态是否判断
//		List<Code> dataList=new ArrayList<Code>();
//		Code code = new Code();
//		code.setCodeTypeCode(codeTypeCode);
//		dataList.add(code);
//		return dataList;
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
		//gcfg.put("url", "tcssczzqyxx!getQyxxGridData.json");
		gcfg.put("valueField", "PZBH");
		gcfg.put("textField", "PZ");
		gcfg.put("panelWidth", 250);
		return gcfg;
	}
	
	protected List<String> getColNames() {
		List<String> list = new ArrayList<String>();
		list.add("品种编号");
		list.add("品种名称");
		return list;
    }
	
	protected List<Map<String, Object>> getColModel() {
		List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
		Map<String, Object> item =null;

		item = new HashMap<String, Object>();
		item.put("name", "PZBH");
		item.put("width", 60);
		item.put("align", "left");
		colModel.add(item);

		item = new HashMap<String, Object>();
		item.put("name", "PZ");
		item.put("width", 100);
		item.put("align", "left");
		colModel.add(item);

		return colModel;
    }
}
